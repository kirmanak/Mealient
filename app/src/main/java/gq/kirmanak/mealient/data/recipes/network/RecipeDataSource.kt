package gq.kirmanak.mealient.data.recipes.network

import gq.kirmanak.mealient.datasource.models.FullRecipeInfo
import gq.kirmanak.mealient.datasource.models.GetRecipeSummaryResponse

interface RecipeDataSource {
    suspend fun requestRecipes(start: Int, limit: Int): List<GetRecipeSummaryResponse>

    suspend fun requestRecipeInfo(slug: String): FullRecipeInfo

    suspend fun getFavoriteRecipes(): List<String>

    suspend fun updateIsRecipeFavorite(recipeSlug: String, isFavorite: Boolean)

    suspend fun deleteRecipe(recipeSlug: String)
}