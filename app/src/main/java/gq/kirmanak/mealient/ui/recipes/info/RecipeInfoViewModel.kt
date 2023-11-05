package gq.kirmanak.mealient.ui.recipes.info

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.data.recipes.RecipeRepo
import gq.kirmanak.mealient.data.recipes.impl.RecipeImageUrlProvider
import gq.kirmanak.mealient.logging.Logger
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class RecipeInfoViewModel @Inject constructor(
    private val recipeRepo: RecipeRepo,
    private val logger: Logger,
    private val recipeImageUrlProvider: RecipeImageUrlProvider,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val args = RecipeInfoFragmentArgs.fromSavedStateHandle(savedStateHandle)
    private val _uiState = flow {
        logger.v { "Initializing UI state with args = $args" }
        val recipeInfo = recipeRepo.loadRecipeInfo(args.recipeId)
        val slug = recipeInfo?.recipeSummaryEntity?.imageId
        val imageUrl = slug?.let { recipeImageUrlProvider.generateImageUrl(slug) }
        val state = recipeInfo?.let { entity ->
            RecipeInfoUiState(
                showIngredients = entity.recipeIngredients.isNotEmpty(),
                showInstructions = entity.recipeInstructions.isNotEmpty(),
                summaryEntity = entity.recipeSummaryEntity,
                recipeIngredients = entity.recipeIngredients,
                recipeInstructions = entity.recipeInstructions,
                title = entity.recipeSummaryEntity.name,
                description = entity.recipeSummaryEntity.description,
                disableAmounts = entity.recipeEntity.disableAmounts,
                imageUrl = imageUrl,
            )
        } ?: RecipeInfoUiState()
        emit(state)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, RecipeInfoUiState())

    val uiState: StateFlow<RecipeInfoUiState> = _uiState

}
