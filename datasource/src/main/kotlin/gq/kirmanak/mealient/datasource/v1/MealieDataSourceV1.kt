package gq.kirmanak.mealient.datasource.v1

import gq.kirmanak.mealient.datasource.v0.models.AddRecipeRequestV0
import gq.kirmanak.mealient.datasource.v1.models.GetRecipeResponseV1
import gq.kirmanak.mealient.datasource.v1.models.GetRecipeSummaryResponseV1
import gq.kirmanak.mealient.datasource.v1.models.VersionResponseV1

interface MealieDataSourceV1 {

    suspend fun addRecipe(
        baseUrl: String,
        token: String?,
        recipe: AddRecipeRequestV0,
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
    ): VersionResponseV1

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
    ): GetRecipeResponseV1
}