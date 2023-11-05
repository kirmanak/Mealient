package gq.kirmanak.mealient.data.recipes.network

import gq.kirmanak.mealient.datasource.models.GetRecipeResponse
import gq.kirmanak.mealient.datasource.models.GetRecipeSummaryResponse

interface RecipeDataSource {
    suspend fun requestRecipes(start: Int, limit: Int): List<GetRecipeSummaryResponse>

    suspend fun requestRecipe(slug: String): GetRecipeResponse

    suspend fun getFavoriteRecipes(): List<String>

    suspend fun updateIsRecipeFavorite(recipeSlug: String, isFavorite: Boolean)

    suspend fun deleteRecipe(recipeSlug: String)
}