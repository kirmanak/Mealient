package gq.kirmanak.mealient.datasource.v1

import gq.kirmanak.mealient.datasource.v1.models.CreateApiTokenRequestV1
import gq.kirmanak.mealient.datasource.v1.models.CreateApiTokenResponseV1
import gq.kirmanak.mealient.datasource.v1.models.CreateRecipeRequestV1
import gq.kirmanak.mealient.datasource.v1.models.GetRecipeResponseV1
import gq.kirmanak.mealient.datasource.v1.models.GetRecipeSummaryResponseV1
import gq.kirmanak.mealient.datasource.v1.models.GetShoppingListsSummaryResponseV1
import gq.kirmanak.mealient.datasource.v1.models.GetUserInfoResponseV1
import gq.kirmanak.mealient.datasource.v1.models.ParseRecipeURLRequestV1
import gq.kirmanak.mealient.datasource.v1.models.UpdateRecipeRequestV1
import gq.kirmanak.mealient.datasource.v1.models.VersionResponseV1

interface MealieDataSourceV1 {

    suspend fun createRecipe(
        recipe: CreateRecipeRequestV1,
    ): String

    suspend fun updateRecipe(
        slug: String,
        recipe: UpdateRecipeRequestV1,
    ): GetRecipeResponseV1

    /**
     * Tries to acquire authentication token using the provided credentials
     */
    suspend fun authenticate(
        username: String,
        password: String,
    ): String

    suspend fun getVersionInfo(
    ): VersionResponseV1

    suspend fun requestRecipes(
        page: Int,
        perPage: Int,
    ): List<GetRecipeSummaryResponseV1>

    suspend fun requestRecipeInfo(
        slug: String,
    ): GetRecipeResponseV1

    suspend fun parseRecipeFromURL(
        request: ParseRecipeURLRequestV1,
    ): String

    suspend fun createApiToken(
        request: CreateApiTokenRequestV1,
    ): CreateApiTokenResponseV1

    suspend fun requestUserInfo(): GetUserInfoResponseV1

    suspend fun removeFavoriteRecipe(userId: String, recipeSlug: String)

    suspend fun addFavoriteRecipe(userId: String, recipeSlug: String)

    suspend fun deleteRecipe(slug: String)

    suspend fun getShoppingLists(): List<GetShoppingListsSummaryResponseV1>
}