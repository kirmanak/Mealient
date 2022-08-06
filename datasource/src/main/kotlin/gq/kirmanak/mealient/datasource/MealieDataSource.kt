package gq.kirmanak.mealient.datasource

import gq.kirmanak.mealient.datasource.models.AddRecipeRequest
import gq.kirmanak.mealient.datasource.models.GetRecipeResponse
import gq.kirmanak.mealient.datasource.models.GetRecipeSummaryResponse
import gq.kirmanak.mealient.datasource.models.VersionResponse

interface MealieDataSource {

    suspend fun addRecipe(
        baseUrl: String,
        token: String?,
        recipe: AddRecipeRequest,
    ): String

    /**
     * Tries to acquire authentication token using the provided credentials
     */
    suspend fun authenticate(
        baseUrl: String,
        username: String,
        password: String,
    ): String

    suspend fun getVersionInfo(
        baseUrl: String,
    ): VersionResponse

    suspend fun requestRecipes(
        baseUrl: String,
        token: String?,
        start: Int = 0,
        limit: Int = 9999,
    ): List<GetRecipeSummaryResponse>

    suspend fun requestRecipeInfo(
        baseUrl: String,
        token: String?,
        slug: String,
    ): GetRecipeResponse
}