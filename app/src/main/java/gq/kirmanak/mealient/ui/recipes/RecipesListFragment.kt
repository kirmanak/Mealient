package gq.kirmanak.mealient.ui.recipes

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.architecture.configuration.valueUpdatesOnly
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.databinding.FragmentRecipesListBinding
import gq.kirmanak.mealient.datasource.NetworkError
import gq.kirmanak.mealient.extensions.*
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.ui.CheckableMenuItem
import gq.kirmanak.mealient.ui.activity.MainActivityViewModel
import gq.kirmanak.mealient.ui.recipes.RecipesListFragmentDirections.Companion.actionRecipesFragmentToRecipeInfoFragment
import gq.kirmanak.mealient.ui.recipes.images.RecipePreloaderFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@AndroidEntryPoint
class RecipesListFragment : Fragment(R.layout.fragment_recipes_list) {

    private val binding by viewBinding(FragmentRecipesListBinding::bind)
    private val viewModel by viewModels<RecipesListViewModel>()
    private val activityViewModel by activityViewModels<MainActivityViewModel>()

    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var recipePagingAdapterFactory: RecipesPagingAdapter.Factory

    @Inject
    lateinit var recipePreloaderFactory: RecipePreloaderFactory

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logger.v { "onViewCreated() called with: view = $view, savedInstanceState = $savedInstanceState" }
        activityViewModel.updateUiState {
            it.copy(
                navigationVisible = true,
                searchVisible = true,
                checkedMenuItem = CheckableMenuItem.RecipesList,
            )
        }
        collectWhenViewResumed(viewModel.showFavoriteIcon) { showFavoriteIcon ->
            setupRecipeAdapter(showFavoriteIcon)
        }
        collectWhenViewResumed(viewModel.deleteRecipeResult) {
            logger.d { "Delete recipe result is $it" }
            if (it.isFailure) {
                showLongToast(R.string.fragment_recipes_delete_recipe_failed)
            }
        }
        hideKeyboardOnScroll()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun hideKeyboardOnScroll() {
        binding.recipes.setOnTouchListener { _, _ ->
            activityViewModel.clearSearchViewFocus()
            false
        }
    }

    private fun navigateToRecipeInfo(id: String) {
        logger.v { "navigateToRecipeInfo() called with: id = $id" }
        val directions = actionRecipesFragmentToRecipeInfoFragment(id)
        findNavController().navigate(directions)
    }

    private fun onRecipeClicked(recipe: RecipeSummaryEntity) {
        logger.v { "onRecipeClicked() called with: recipe = $recipe" }
        binding.progress.isVisible = true
        viewModel.refreshRecipeInfo(recipe.slug).observe(viewLifecycleOwner) {
            binding.progress.isVisible = false
            if (!isNavigatingSomewhere()) navigateToRecipeInfo(recipe.remoteId)
        }
    }

    private fun isNavigatingSomewhere(): Boolean {
        logger.v { "isNavigatingSomewhere() called" }
        return findNavController().currentDestination?.id != R.id.recipesListFragment
    }

    private fun setupRecipeAdapter(showFavoriteIcon: Boolean) {
        logger.v { "setupRecipeAdapter() called" }

        val recipesAdapter = recipePagingAdapterFactory.build(showFavoriteIcon) {
            when (it) {
                is RecipeViewHolder.ClickEvent.FavoriteClick -> {
                    onFavoriteClick(it)
                }
                is RecipeViewHolder.ClickEvent.RecipeClick -> {
                    onRecipeClicked(it.recipeSummaryEntity)
                }
                is RecipeViewHolder.ClickEvent.DeleteClick -> {
                    onDeleteClick(it)
                }
            }
        }

        with(binding.recipes) {
            adapter = recipesAdapter
            addOnScrollListener(recipePreloaderFactory.create(recipesAdapter))
        }

        collectWhenViewResumed(viewModel.pagingData) {
            logger.v { "setupRecipeAdapter: received data update" }
            recipesAdapter.submitData(lifecycle, it)
        }

        collectWhenViewResumed(recipesAdapter.onPagesUpdatedFlow) {
            logger.v { "setupRecipeAdapter: pages updated" }
            binding.refresher.isRefreshing = false
            binding.emptyListText.isVisible = recipesAdapter.itemCount == 0
        }

        collectWhenViewResumed(recipesAdapter.appendPaginationEnd()) {
            logger.v { "onPaginationEnd() called" }
            showLongToast(R.string.fragment_recipes_last_page_loaded_toast)
        }

        collectWhenViewResumed(recipesAdapter.sourceIsRefreshing()) { disableSwipeRefresh ->
            logger.v { "setupRecipeAdapter: changing refresher enabled state to ${!disableSwipeRefresh}" }
            binding.refresher.isEnabled = !disableSwipeRefresh
        }

        collectWhenViewResumed(recipesAdapter.refreshErrors()) {
            onLoadFailure(it)
        }

        collectWhenViewResumed(binding.refresher.refreshRequestFlow(logger)) {
            logger.v { "setupRecipeAdapter: received refresh request" }
            recipesAdapter.refresh()
        }
    }

    private fun onDeleteClick(event: RecipeViewHolder.ClickEvent) {
        logger.v { "onDeleteClick() called with: event = $event" }
        val entity = event.recipeSummaryEntity
        val message = getString(
            R.string.fragment_recipes_delete_recipe_confirm_dialog_message, entity.name
        )
        val onPositiveClick = DialogInterface.OnClickListener { _, _ ->
            viewModel.onDeleteConfirm(entity)
        }
        val positiveBtnResId = R.string.fragment_recipes_delete_recipe_confirm_dialog_positive_btn
        val titleResId = R.string.fragment_recipes_delete_recipe_confirm_dialog_title
        val negativeBtnResId = R.string.fragment_recipes_delete_recipe_confirm_dialog_negative_btn
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(titleResId)
            .setMessage(message)
            .setPositiveButton(positiveBtnResId, onPositiveClick)
            .setNegativeButton(negativeBtnResId) { _, _ -> }
            .show()

    }

    private fun onFavoriteClick(event: RecipeViewHolder.ClickEvent) {
        logger.v { "onFavoriteClick() called with: event = $event" }
        viewModel.onFavoriteIconClick(event.recipeSummaryEntity).observe(viewLifecycleOwner) {
            logger.d { "onFavoriteClick: result is $it" }
            if (it.isFailure) {
                showLongToast(R.string.fragment_recipes_favorite_update_failed)
            } else {
                val name = event.recipeSummaryEntity.name
                val isFavorite = it.getOrThrow()
                val message = if (isFavorite) {
                    getString(R.string.fragment_recipes_favorite_added, name)
                } else {
                    getString(R.string.fragment_recipes_favorite_removed, name)
                }
                showLongToast(message)
            }
        }
    }

    private fun onLoadFailure(error: Throwable) {
        logger.w(error) { "onLoadFailure() called" }
        val reason = error.toLoadErrorReasonText()?.let { getString(it) }
        val toastText = if (reason == null) {
            getString(R.string.fragment_recipes_load_failure_toast_no_reason)
        } else {
            getString(R.string.fragment_recipes_load_failure_toast, reason)
        }
        showLongToast(toastText)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        logger.v { "onDestroyView() called" }
        // Prevent RV leaking through mObservers list in adapter
        binding.recipes.adapter = null
    }
}

@StringRes
private fun Throwable.toLoadErrorReasonText(): Int? = when (this) {
    is NetworkError.Unauthorized -> R.string.fragment_recipes_load_failure_toast_unauthorized
    is NetworkError.NoServerConnection -> R.string.fragment_recipes_load_failure_toast_no_connection
    is NetworkError.NotMealie, is NetworkError.MalformedUrl -> R.string.fragment_recipes_load_failure_toast_unexpected_response
    else -> null
}

private fun <T : Any, VH : RecyclerView.ViewHolder> PagingDataAdapter<T, VH>.refreshErrors(): Flow<Throwable> {
    return loadStateFlow.map { it.refresh }.valueUpdatesOnly().filterIsInstance<LoadState.Error>()
        .map { it.error }
}

private fun <T : Any, VH : RecyclerView.ViewHolder> PagingDataAdapter<T, VH>.appendPaginationEnd(): Flow<Unit> {
    return loadStateFlow.map { it.append.endOfPaginationReached }.valueUpdatesOnly().filter { it }
        .map { }
}

private fun <T : Any, VH : RecyclerView.ViewHolder> PagingDataAdapter<T, VH>.sourceIsRefreshing(): Flow<Boolean> {
    return loadStateFlow.map { it.source.refresh !is LoadState.NotLoading }.valueUpdatesOnly()
}
