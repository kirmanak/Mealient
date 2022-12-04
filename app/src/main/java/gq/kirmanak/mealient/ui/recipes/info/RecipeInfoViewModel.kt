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
        val state = recipeRepo.loadRecipeInfo(args.recipeId)?.let { entity ->
            RecipeInfoUiState(
                showIngredients = entity.recipeIngredients.isNotEmpty(),
                showInstructions = entity.recipeInstructions.isNotEmpty(),
                summaryEntity = entity.recipeSummaryEntity,
                recipeIngredients = entity.recipeIngredients.filter { it.note.isNotBlank() },
                recipeInstructions = entity.recipeInstructions.filter { it.text.isNotBlank() },
                title = entity.recipeSummaryEntity.name,
                description = entity.recipeSummaryEntity.description,
                disableAmounts = entity.recipeEntity.disableAmounts,
            )
        } ?: RecipeInfoUiState()
        emit(state)
    }

}
