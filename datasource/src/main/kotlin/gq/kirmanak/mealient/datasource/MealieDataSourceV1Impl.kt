package gq.kirmanak.mealient.datasource

import gq.kirmanak.mealient.datasource.models.AddRecipeRequest
import gq.kirmanak.mealient.datasource.models.GetRecipeResponse
import gq.kirmanak.mealient.datasource.models.GetRecipeSummaryResponse
import gq.kirmanak.mealient.datasource.models.VersionResponse

class MealieDataSourceV1Impl : MealieDataSourceV1 {
    override suspend fun addRecipe(
        baseUrl: String,
        token: String?,
        recipe: AddRecipeRequest
    ): String {
        TODO("Not yet implemented")
    }

    override suspend fun authenticate(baseUrl: String, username: String, password: String): String {
        TODO("Not yet implemented")
    }

    override suspend fun getVersionInfo(baseUrl: String): VersionResponse {
        TODO("Not yet implemented")
    }

    override suspend fun requestRecipes(
        baseUrl: String,
        token: String?,
        start: Int,
        limit: Int
    ): List<GetRecipeSummaryResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun requestRecipeInfo(
        baseUrl: String,
        token: String?,
        slug: String
    ): GetRecipeResponse {
        TODO("Not yet implemented")
    }
}