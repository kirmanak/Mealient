package gq.kirmanak.mealient.data.recipes

import androidx.paging.Pager
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeWithSummaryAndIngredientsAndInstructions

interface RecipeRepo {

    fun createPager(): Pager<Int, RecipeSummaryEntity>

    suspend fun clearLocalData()

    suspend fun refreshRecipeInfo(recipeSlug: String): Result<Unit>

    suspend fun loadRecipeInfo(recipeId: String): RecipeWithSummaryAndIngredientsAndInstructions?

    fun updateNameQuery(name: String?)

    suspend fun refreshRecipes()

    suspend fun updateIsRecipeFavorite(recipeSlug: String, isFavorite: Boolean): Result<Boolean>

    suspend fun deleteRecipe(entity: RecipeSummaryEntity): Result<Unit>
}