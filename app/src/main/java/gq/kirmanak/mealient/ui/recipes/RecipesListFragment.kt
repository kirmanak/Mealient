package gq.kirmanak.mealient.ui.recipes

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
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.databinding.FragmentRecipesListBinding
import gq.kirmanak.mealient.datasource.NetworkError
import gq.kirmanak.mealient.extensions.collectWhenViewResumed
import gq.kirmanak.mealient.extensions.refreshRequestFlow
import gq.kirmanak.mealient.extensions.showLongToast
import gq.kirmanak.mealient.extensions.valueUpdatesOnly
import gq.kirmanak.mealient.logging.Logger
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
                loginButtonVisible = true,
                titleVisible = false,
                navigationVisible = true,
                searchVisible = true,
            )
        }
        setupRecipeAdapter()
    }

    private fun navigateToRecipeInfo(id: String) {
        logger.v { "navigateToRecipeInfo() called with: id = $id" }
        val directions = actionRecipesFragmentToRecipeInfoFragment(id)
        findNavController().navigate(directions)
    }

    private fun onRecipeClicked(recipe: RecipeSummaryEntity) {
        logger.v { "onRecipeClicked() called with: recipe = $recipe" }
        binding.progress.isVisible = true
        viewModel.refreshRecipeInfo(recipe.slug).observe(viewLifecycleOwner) { result ->
            binding.progress.isVisible = false
            if (result.isSuccess && !isNavigatingSomewhere()) {
                navigateToRecipeInfo(recipe.remoteId)
            }
        }
    }

    private fun isNavigatingSomewhere(): Boolean {
        logger.v { "isNavigatingSomewhere() called" }
        val label = findNavController().currentDestination?.label
        logger.d { "isNavigatingSomewhere: current destination is $label" }
        return label != "fragment_recipes"
    }

    private fun setupRecipeAdapter() {
        logger.v { "setupRecipeAdapter() called" }

        val recipesAdapter = recipePagingAdapterFactory.build { onRecipeClicked(it) }

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
        }

        collectWhenViewResumed(recipesAdapter.appendPaginationEnd()) {
            logger.v { "onPaginationEnd() called" }
            showLongToast(R.string.fragment_recipes_last_page_loaded_toast)
        }

        collectWhenViewResumed(recipesAdapter.refreshErrors()) {
            onLoadFailure(it)
        }

        collectWhenViewResumed(binding.refresher.refreshRequestFlow(logger)) {
            logger.v { "setupRecipeAdapter: received refresh request" }
            recipesAdapter.refresh()
        }

        viewModel.isAuthorized.observe(viewLifecycleOwner) { isAuthorized ->
            logger.v { "setupRecipeAdapter: isAuthorized changed to $isAuthorized" }
            if (isAuthorized != null) {
                if (isAuthorized) recipesAdapter.refresh()
                // else is ignored to avoid the removal of the non-public recipes
                viewModel.onAuthorizationChangeHandled()
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
