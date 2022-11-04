package gq.kirmanak.mealient.ui.recipes

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.databinding.FragmentRecipesBinding
import gq.kirmanak.mealient.datasource.NetworkError
import gq.kirmanak.mealient.extensions.collectWhenViewResumed
import gq.kirmanak.mealient.extensions.launchIn
import gq.kirmanak.mealient.extensions.refreshRequestFlow
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.ui.activity.MainActivityViewModel
import gq.kirmanak.mealient.ui.recipes.images.RecipeImageLoader
import gq.kirmanak.mealient.ui.recipes.images.RecipePreloaderFactory
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@AndroidEntryPoint
class RecipesFragment : Fragment(R.layout.fragment_recipes) {

    private val binding by viewBinding(FragmentRecipesBinding::bind)
    private val viewModel by viewModels<RecipeViewModel>()
    private val activityViewModel by activityViewModels<MainActivityViewModel>()

    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var recipeImageLoader: RecipeImageLoader

    @Inject
    lateinit var recipePagingAdapterFactory: RecipesPagingAdapter.Factory

    @Inject
    lateinit var recipePreloaderFactory: RecipePreloaderFactory

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logger.v { "onViewCreated() called with: view = $view, savedInstanceState = $savedInstanceState" }
        activityViewModel.updateUiState {
            it.copy(loginButtonVisible = true, titleVisible = false, navigationVisible = true)
        }
        setupRecipeAdapter()
    }

    private fun navigateToRecipeInfo(recipeSummaryEntity: RecipeSummaryEntity) {
        logger.v { "navigateToRecipeInfo() called with: recipeSummaryEntity = $recipeSummaryEntity" }
        findNavController().navigate(
            RecipesFragmentDirections.actionRecipesFragmentToRecipeInfoFragment(
                recipeSlug = recipeSummaryEntity.slug, recipeId = recipeSummaryEntity.remoteId
            )
        )
    }

    private fun setupRecipeAdapter() {
        logger.v { "setupRecipeAdapter() called" }

        val recipesAdapter = recipePagingAdapterFactory.build(
            recipeImageLoader = recipeImageLoader, clickListener = ::navigateToRecipeInfo
        )

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

        observeLoadStateChanges(recipesAdapter)

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

    private fun observeLoadStateChanges(recipesAdapter: RecipesPagingAdapter) {
        recipesAdapter.loadStateFlow
            .map { it.append }
            .distinctUntilChanged()
            .filter { it.endOfPaginationReached }
            .onEach { onPaginationEnd() }
            .launchIn(viewLifecycleOwner)

        recipesAdapter.loadStateFlow
            .map { it.refresh }
            .distinctUntilChanged()
            .filterIsInstance<LoadState.Error>()
            .onEach { onLoadFailure(it.error) }
            .launchIn(viewLifecycleOwner)
    }

    private fun onPaginationEnd() {
        logger.v { "onPaginationEnd() called" }
        Toast.makeText(
            requireContext(),
            getString(R.string.fragment_recipes_last_page_loaded_toast),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun onLoadFailure(error: Throwable) {
        logger.v(error) { "onLoadFailure() called" }

        val reason = when (error) {
            is NetworkError.Unauthorized -> R.string.fragment_recipes_load_failure_toast_unauthorized
            is NetworkError.NoServerConnection -> R.string.fragment_recipes_load_failure_toast_no_connection
            is NetworkError.NotMealie, is NetworkError.MalformedUrl -> R.string.fragment_recipes_load_failure_toast_unexpected_response
            else -> null
        }?.let { getString(it) }

        val generalText = getString(R.string.fragment_recipes_load_failure_toast_general)

        val text = if (reason == null) {
            generalText
        } else {
            getString(R.string.fragment_recipes_load_failure_toast, generalText, reason)
        }

        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        logger.v { "onDestroyView() called" }
        // Prevent RV leaking through mObservers list in adapter
        binding.recipes.adapter = null
    }
}