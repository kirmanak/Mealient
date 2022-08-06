package gq.kirmanak.mealient.data.recipes.network

import gq.kirmanak.mealient.datasource.models.GetRecipeResponse
import gq.kirmanak.mealient.datasource.models.GetRecipeSummaryResponse

interface RecipeDataSource {
    suspend fun requestRecipes(start: Int = 0, limit: Int = 9999): List<GetRecipeSummaryResponse>

    suspend fun requestRecipeInfo(slug: String): GetRecipeResponse
}