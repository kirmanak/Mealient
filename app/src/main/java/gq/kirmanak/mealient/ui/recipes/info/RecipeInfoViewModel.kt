package gq.kirmanak.mealient.ui.recipes.info

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.data.recipes.RecipeRepo
import gq.kirmanak.mealient.logging.Logger
import javax.inject.Inject

@HiltViewModel
class RecipeInfoViewModel @Inject constructor(
    private val recipeRepo: RecipeRepo,
    private val logger: Logger,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val args = RecipeInfoFragmentArgs.fromSavedStateHandle(savedStateHandle)

    val uiState: LiveData<RecipeInfoUiState> = liveData {
        logger.v { "Initializing UI state with args = $args" }
        val state = recipeRepo.loadRecipeInfoFromDb(args.recipeId, args.recipeSlug)?.let {
            RecipeInfoUiState(
                areIngredientsVisible = it.recipeIngredients.isNotEmpty(),
                areInstructionsVisible = it.recipeInstructions.isNotEmpty(),
                recipeInfo = it,
            )
        } ?: RecipeInfoUiState()
        emit(state)
    }

}
