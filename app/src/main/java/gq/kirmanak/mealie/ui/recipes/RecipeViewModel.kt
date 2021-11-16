package gq.kirmanak.mealie.ui.recipes

import android.widget.ImageView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealie.data.recipes.RecipeImageLoader
import gq.kirmanak.mealie.data.recipes.RecipeRepo
import gq.kirmanak.mealie.data.recipes.db.RecipeEntity
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    recipeRepo: RecipeRepo,
    private val recipeImageLoader: RecipeImageLoader
) : ViewModel() {
    val recipeFlow = recipeRepo.createPager().flow

    fun loadRecipeImage(view: ImageView, recipe: RecipeEntity?) {
        viewModelScope.launch {
            recipeImageLoader.loadRecipeImage(view, recipe?.slug)
        }
    }
}