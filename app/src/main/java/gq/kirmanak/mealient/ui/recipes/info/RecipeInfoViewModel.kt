package gq.kirmanak.mealient.ui.recipes.info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.data.recipes.RecipeRepo
import gq.kirmanak.mealient.extensions.runCatchingExceptCancel
import gq.kirmanak.mealient.logging.Logger
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeInfoViewModel @Inject constructor(
    private val recipeRepo: RecipeRepo,
    private val logger: Logger,
) : ViewModel() {

    private val _uiState = MutableLiveData(RecipeInfoUiState())
    val uiState: LiveData<RecipeInfoUiState> get() = _uiState

    fun loadRecipeInfo(recipeId: Long, recipeSlug: String) {
        logger.v { "loadRecipeInfo() called with: recipeId = $recipeId, recipeSlug = $recipeSlug" }
        _uiState.value = RecipeInfoUiState()
        viewModelScope.launch {
            runCatchingExceptCancel { recipeRepo.loadRecipeInfo(recipeId, recipeSlug) }
                .onSuccess {
                    logger.d { "loadRecipeInfo: received recipe info = $it" }
                    _uiState.value = RecipeInfoUiState(
                        areIngredientsVisible = it.recipeIngredients.isNotEmpty(),
                        areInstructionsVisible = it.recipeInstructions.isNotEmpty(),
                        recipeInfo = it,
                    )
                }
                .onFailure { logger.e(it) { "loadRecipeInfo: can't load recipe info" } }
        }
    }
}
