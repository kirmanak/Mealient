package gq.kirmanak.mealient.datasource.v0

import gq.kirmanak.mealient.datasource.v0.models.AddRecipeRequestV0
import gq.kirmanak.mealient.datasource.v0.models.CreateApiTokenRequestV0
import gq.kirmanak.mealient.datasource.v0.models.GetRecipeResponseV0
import gq.kirmanak.mealient.datasource.v0.models.GetRecipeSummaryResponseV0
import gq.kirmanak.mealient.datasource.v0.models.GetUserInfoResponseV0
import gq.kirmanak.mealient.datasource.v0.models.ParseRecipeURLRequestV0
import gq.kirmanak.mealient.datasource.v0.models.VersionResponseV0

interface MealieDataSourceV0 {

    suspend fun addRecipe(
        recipe: AddRecipeRequestV0,
    ): String

    /**
     * Tries to acquire authentication token using the provided credentials
     */
    suspend fun authenticate(
        username: String,
        password: String,
    ): String

    suspend fun getVersionInfo(): VersionResponseV0

    suspend fun requestRecipes(
        start: Int,
        limit: Int,
    ): List<GetRecipeSummaryResponseV0>

    suspend fun requestRecipeInfo(
        slug: String,
    ): GetRecipeResponseV0

    suspend fun parseRecipeFromURL(
        request: ParseRecipeURLRequestV0,
    ): String

    suspend fun createApiToken(
        request: CreateApiTokenRequestV0,
    ): String

    suspend fun requestUserInfo(): GetUserInfoResponseV0

    suspend fun removeFavoriteRecipe(userId: Int, recipeSlug: String)

    suspend fun addFavoriteRecipe(userId: Int, recipeSlug: String)
}