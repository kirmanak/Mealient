package gq.kirmanak.mealie.data.recipes

import androidx.paging.Pager
import gq.kirmanak.mealie.data.recipes.db.entity.RecipeSummaryEntity
import gq.kirmanak.mealie.data.recipes.impl.FullRecipeInfo

interface RecipeRepo {
    fun createPager(): Pager<Int, RecipeSummaryEntity>

    suspend fun clearLocalData()

    suspend fun loadRecipeInfo(recipeId: Long, recipeSlug: String): FullRecipeInfo
}