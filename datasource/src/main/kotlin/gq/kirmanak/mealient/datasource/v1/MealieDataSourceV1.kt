package gq.kirmanak.mealient.datasource.v1

import gq.kirmanak.mealient.datasource.v1.models.CreateRecipeRequestV1
import gq.kirmanak.mealient.datasource.v1.models.GetRecipeResponseV1
import gq.kirmanak.mealient.datasource.v1.models.GetRecipeSummaryResponseV1
import gq.kirmanak.mealient.datasource.v1.models.ParseRecipeURLRequestV1
import gq.kirmanak.mealient.datasource.v1.models.UpdateRecipeRequestV1
import gq.kirmanak.mealient.datasource.v1.models.VersionResponseV1

interface MealieDataSourceV1 {

    suspend fun createRecipe(
        baseUrl: String,
        token: String?,
        recipe: CreateRecipeRequestV1,
    ): String

    suspend fun updateRecipe(
        baseUrl: String,
        token: String?,
        slug: String,
        recipe: UpdateRecipeRequestV1,
    ): GetRecipeResponseV1

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
        page: Int,
        perPage: Int,
    ): List<GetRecipeSummaryResponseV1>

    suspend fun requestRecipeInfo(
        baseUrl: String,
        token: String?,
        slug: String,
    ): GetRecipeResponseV1

    suspend fun parseRecipeFromURL(
        baseUrl: String,
        token: String?,
        request: ParseRecipeURLRequestV1,
    ): String
}