package gq.kirmanak.mealient.data.recipes

import androidx.paging.Pager
import gq.kirmanak.mealient.database.recipe.entity.FullRecipeEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity

interface RecipeRepo {

    fun createPager(): Pager<Int, RecipeSummaryEntity>

    suspend fun clearLocalData()

    suspend fun refreshRecipeInfo(recipeSlug: String): Result<Unit>

    suspend fun loadRecipeInfo(recipeId: String): FullRecipeEntity?

    fun updateNameQuery(name: String?)

    suspend fun refreshRecipes()

    suspend fun updateIsRecipeFavorite(recipeSlug: String, isFavorite: Boolean): Result<Unit>

    suspend fun deleteRecipe(recipeSlug: String): Result<Unit>
}