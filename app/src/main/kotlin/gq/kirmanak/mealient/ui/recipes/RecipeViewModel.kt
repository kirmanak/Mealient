package gq.kirmanak.mealient.ui.recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.data.recipes.RecipeRepo
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(recipeRepo: RecipeRepo) : ViewModel() {

    val pagingData = recipeRepo.createPager().flow.cachedIn(viewModelScope)

}