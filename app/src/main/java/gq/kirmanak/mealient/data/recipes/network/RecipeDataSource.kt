package gq.kirmanak.mealient.data.recipes.network

import gq.kirmanak.mealient.datasource.models.GetRecipeResponse

interface RecipeDataSource {
    suspend fun requestRecipes(start: Int, limit: Int): List<RecipeSummaryInfo>

    suspend fun requestRecipeInfo(slug: String): GetRecipeResponse
}