package gq.kirmanak.mealient.datasource.v1

import gq.kirmanak.mealient.datasource.models.AddRecipeRequest
import gq.kirmanak.mealient.datasource.models.GetRecipeResponse
import gq.kirmanak.mealient.datasource.models.VersionResponse
import gq.kirmanak.mealient.datasource.v1.models.GetRecipeSummaryResponseV1

interface MealieDataSourceV1 {

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
        start: Int,
        limit: Int,
    ): List<GetRecipeSummaryResponseV1>

    suspend fun requestRecipeInfo(
        baseUrl: String,
        token: String?,
        slug: String,
    ): GetRecipeResponse
}