package gq.kirmanak.mealient.data.recipes

import androidx.paging.Pager
import gq.kirmanak.mealient.database.recipe.entity.FullRecipeEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity

interface RecipeRepo {
    fun createPager(): Pager<Int, RecipeSummaryEntity>

    suspend fun clearLocalData()

    suspend fun loadRecipeInfo(recipeId: String, recipeSlug: String): FullRecipeEntity

    suspend fun loadRecipeInfoFromDb(recipeId: String, recipeSlug: String): FullRecipeEntity?
}