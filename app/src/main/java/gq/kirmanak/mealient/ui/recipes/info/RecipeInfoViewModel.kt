package gq.kirmanak.mealient.ui.recipes.info

import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.data.recipes.RecipeImageLoader
import gq.kirmanak.mealient.data.recipes.RecipeRepo
import gq.kirmanak.mealient.data.recipes.impl.FullRecipeInfo
import gq.kirmanak.mealient.extensions.runCatchingExceptCancel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RecipeInfoViewModel
@Inject
constructor(
    private val recipeRepo: RecipeRepo,
    private val recipeImageLoader: RecipeImageLoader,
    private val recipeIngredientsAdapter: RecipeIngredientsAdapter,
    private val recipeInstructionsAdapter: RecipeInstructionsAdapter,
) : ViewModel() {

    private val _recipeInfo = MutableLiveData<FullRecipeInfo>()
    val recipeInfo: LiveData<FullRecipeInfo>
        get() = _recipeInfo
    private val _listsVisibility = MutableLiveData(RecipeInfoListsVisibility())
    val listsVisibility: LiveData<RecipeInfoListsVisibility>
        get() = _listsVisibility

    fun loadRecipeImage(view: ImageView, recipeSlug: String) {
        Timber.v("loadRecipeImage() called with: view = $view, recipeSlug = $recipeSlug")
        viewModelScope.launch { recipeImageLoader.loadRecipeImage(view, recipeSlug) }
    }

    fun loadRecipeInfo(recipeId: Long, recipeSlug: String) {
        Timber.v("loadRecipeInfo() called with: recipeId = $recipeId, recipeSlug = $recipeSlug")
        _listsVisibility.value = RecipeInfoListsVisibility()
        recipeIngredientsAdapter.submitList(null)
        recipeInstructionsAdapter.submitList(null)
        viewModelScope.launch {
            runCatchingExceptCancel { recipeRepo.loadRecipeInfo(recipeId, recipeSlug) }
                .onSuccess {
                    Timber.d("loadRecipeInfo: received recipe info = $it")
                    _recipeInfo.value = it
                    recipeIngredientsAdapter.submitList(it.recipeIngredients)
                    recipeInstructionsAdapter.submitList(it.recipeInstructions)
                    _listsVisibility.value =
                        RecipeInfoListsVisibility(
                            areIngredientsVisible = it.recipeIngredients.isNotEmpty(),
                            areInstructionsVisible = it.recipeInstructions.isNotEmpty()
                        )
                }
                .onFailure { Timber.e(it, "loadRecipeInfo: can't load recipe info") }
        }
    }
}
