package gq.kirmanak.mealie.ui.recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealie.data.recipes.RecipeRepo
import gq.kirmanak.mealie.data.recipes.db.RecipeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(private val recipeRepo: RecipeRepo) : ViewModel() {
    private val pager: Pager<Int, RecipeEntity> by lazy { recipeRepo.createPager() }
    val recipeFlow: Flow<PagingData<RecipeEntity>> by lazy { pager.flow.cachedIn(viewModelScope) }
}