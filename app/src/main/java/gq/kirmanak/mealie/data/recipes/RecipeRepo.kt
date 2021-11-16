package gq.kirmanak.mealie.data.recipes

import androidx.paging.Pager
import gq.kirmanak.mealie.data.recipes.db.RecipeEntity

interface RecipeRepo {
    fun createPager(): Pager<Int, RecipeEntity>

    suspend fun clearLocalData()
}