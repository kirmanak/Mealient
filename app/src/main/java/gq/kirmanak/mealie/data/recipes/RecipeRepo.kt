package gq.kirmanak.mealie.data.recipes

import androidx.paging.Pager
import gq.kirmanak.mealie.data.recipes.db.entity.RecipeSummaryEntity

interface RecipeRepo {
    fun createPager(): Pager<Int, RecipeSummaryEntity>

    suspend fun clearLocalData()
}