package gq.kirmanak.mealient.data.recipes.network

import gq.kirmanak.mealient.datasource.models.GetRecipeResponse
import gq.kirmanak.mealient.datasource.models.GetRecipeSummaryResponse

interface RecipeDataSource {
    suspend fun requestRecipes(start: Int, limit: Int): List<GetRecipeSummaryResponse>

    suspend fun requestRecipeInfo(slug: String): GetRecipeResponse
}