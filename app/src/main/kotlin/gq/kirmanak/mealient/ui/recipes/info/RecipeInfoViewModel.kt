package gq.kirmanak.mealient.ui.recipes.info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.data.recipes.RecipeRepo
import gq.kirmanak.mealient.extensions.runCatchingExceptCancel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RecipeInfoViewModel @Inject constructor(
    private val recipeRepo: RecipeRepo,
) : ViewModel() {

    private val _uiState = MutableLiveData(RecipeInfoUiState())
    val uiState: LiveData<RecipeInfoUiState> get() = _uiState

    fun loadRecipeInfo(recipeId: Long, recipeSlug: String) {
        Timber.v("loadRecipeInfo() called with: recipeId = $recipeId, recipeSlug = $recipeSlug")
        _uiState.value = RecipeInfoUiState()
        viewModelScope.launch {
            runCatchingExceptCancel { recipeRepo.loadRecipeInfo(recipeId, recipeSlug) }
                .onSuccess {
                    Timber.d("loadRecipeInfo: received recipe info = $it")
                    _uiState.value = RecipeInfoUiState(
                        areIngredientsVisible = it.recipeIngredients.isNotEmpty(),
                        areInstructionsVisible = it.recipeInstructions.isNotEmpty(),
                        recipeInfo = it,
                    )
                }
                .onFailure { Timber.e(it, "loadRecipeInfo: can't load recipe info") }
        }
    }
}
