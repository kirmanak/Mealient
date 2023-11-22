package gq.kirmanak.mealient.ui.recipes.list

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.Composable
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.extensions.hideKeyboard
import gq.kirmanak.mealient.ui.BaseComposeFragment
import gq.kirmanak.mealient.ui.CheckableMenuItem
import gq.kirmanak.mealient.ui.activity.MainActivityViewModel
import gq.kirmanak.mealient.ui.recipes.RecipesListViewModel

@AndroidEntryPoint
internal class RecipesListFragment : BaseComposeFragment() {

    private val viewModel by viewModels<RecipesListViewModel>()
    private val activityViewModel by activityViewModels<MainActivityViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activityViewModel.updateUiState {
            it.copy(
                navigationVisible = true,
                searchVisible = true,
                checkedMenuItem = CheckableMenuItem.RecipesList,
            )
        }
    }

    @Composable
    override fun Screen() = RecipesList(
        recipesFlow = viewModel.pagingDataRecipeState,
        onDeleteClick = { viewModel.onDeleteConfirm(it.entity) },
        onFavoriteClick = { onFavoriteButtonClicked(it.entity) },
        onItemClick = { onRecipeClicked(it.entity) },
        onSnackbarShown = { viewModel.onSnackbarShown() },
        snackbarMessageState = viewModel.snackbarState,
    )

    private fun onFavoriteButtonClicked(recipe: RecipeSummaryEntity) {
        viewModel.onFavoriteIconClick(recipe)
    }

    private fun onRecipeClicked(recipe: RecipeSummaryEntity) {
        viewModel.refreshRecipeInfo(recipe.slug).observe(viewLifecycleOwner) {
            if (!isNavigatingSomewhere()) navigateToRecipeInfo(recipe.remoteId)
        }
    }

    private fun isNavigatingSomewhere(): Boolean {
        logger.v { "isNavigatingSomewhere() called" }
        return findNavController().currentDestination?.id != R.id.recipesListFragment
    }

    private fun navigateToRecipeInfo(id: String) {
        logger.v { "navigateToRecipeInfo() called with: id = $id" }
        requireView().hideKeyboard()
        findNavController().navigate(
            RecipesListFragmentDirections.actionRecipesFragmentToRecipeInfoFragment(id)
        )
    }
}