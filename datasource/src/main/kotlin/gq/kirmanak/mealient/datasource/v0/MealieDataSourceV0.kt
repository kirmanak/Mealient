package gq.kirmanak.mealient.datasource.v0

import gq.kirmanak.mealient.datasource.v0.models.AddRecipeRequestV0
import gq.kirmanak.mealient.datasource.v0.models.GetRecipeResponseV0
import gq.kirmanak.mealient.datasource.v0.models.GetRecipeSummaryResponseV0
import gq.kirmanak.mealient.datasource.v0.models.ParseRecipeURLRequestV0
import gq.kirmanak.mealient.datasource.v0.models.VersionResponseV0

interface MealieDataSourceV0 {

    suspend fun addRecipe(
        baseUrl: String,
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
    ): VersionResponseV0

    suspend fun requestRecipes(
        baseUrl: String,
        start: Int,
        limit: Int,
    ): List<GetRecipeSummaryResponseV0>

    suspend fun requestRecipeInfo(
        baseUrl: String,
        slug: String,
    ): GetRecipeResponseV0

    suspend fun parseRecipeFromURL(
        baseUrl: String,
        request: ParseRecipeURLRequestV0,
    ): String
}