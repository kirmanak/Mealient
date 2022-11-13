package gq.kirmanak.mealient.data.recipes.impl

import androidx.paging.PagingSource
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity

interface RecipePagingSourceFactory : () -> PagingSource<Int, RecipeSummaryEntity> {
    fun setQuery(newQuery: String?)
    fun invalidate()
}