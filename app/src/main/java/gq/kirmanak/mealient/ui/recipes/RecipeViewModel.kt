package gq.kirmanak.mealient.ui.recipes

import android.widget.ImageView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.data.recipes.RecipeImageLoader
import gq.kirmanak.mealient.data.recipes.RecipeRepo
import gq.kirmanak.mealient.data.recipes.db.entity.RecipeSummaryEntity
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    recipeRepo: RecipeRepo,
    private val recipeImageLoader: RecipeImageLoader
) : ViewModel() {

    val pagingData = recipeRepo.createPager().flow.cachedIn(viewModelScope)

    fun loadRecipeImage(view: ImageView, recipeSummary: RecipeSummaryEntity?) {
        Timber.v("loadRecipeImage() called with: view = $view, recipeSummary = $recipeSummary")
        viewModelScope.launch {
            recipeImageLoader.loadRecipeImage(view, recipeSummary?.slug)
        }
    }
}