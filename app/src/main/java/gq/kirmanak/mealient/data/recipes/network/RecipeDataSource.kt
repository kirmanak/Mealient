package gq.kirmanak.mealient.data.recipes.network

import gq.kirmanak.mealient.datasource.models.GetRecipeResponse
import gq.kirmanak.mealient.datasource.v1.models.GetRecipeSummaryResponseV1

interface RecipeDataSource {
    suspend fun requestRecipes(start: Int, limit: Int): List<GetRecipeSummaryResponseV1>

    suspend fun requestRecipeInfo(slug: String): GetRecipeResponse
}