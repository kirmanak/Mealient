package gq.kirmanak.mealie.ui.recipes

import android.widget.ImageView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealie.data.recipes.RecipeImageLoader
import gq.kirmanak.mealie.data.recipes.RecipeRepo
import gq.kirmanak.mealie.data.recipes.db.RecipeEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val recipeRepo: RecipeRepo,
    private val recipeImageLoader: RecipeImageLoader
) : ViewModel() {
    private val pager: Pager<Int, RecipeEntity> by lazy { recipeRepo.createPager() }
    val recipeFlow: Flow<PagingData<RecipeEntity>> by lazy { pager.flow.cachedIn(viewModelScope) }

    fun loadRecipeImage(view: ImageView, recipe: RecipeEntity?) {
        viewModelScope.launch {
            recipeImageLoader.loadRecipeImage(view, recipe?.slug)
        }
    }
}