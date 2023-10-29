package gq.kirmanak.mealient.datasource

import gq.kirmanak.mealient.datasource.models.CreateApiTokenRequest
import gq.kirmanak.mealient.datasource.models.CreateApiTokenResponse
import gq.kirmanak.mealient.datasource.models.CreateRecipeRequest
import gq.kirmanak.mealient.datasource.models.CreateShoppingListItemRequest
import gq.kirmanak.mealient.datasource.models.GetFoodsResponse
import gq.kirmanak.mealient.datasource.models.GetRecipeResponse
import gq.kirmanak.mealient.datasource.models.GetRecipeSummaryResponse
import gq.kirmanak.mealient.datasource.models.GetShoppingListResponse
import gq.kirmanak.mealient.datasource.models.GetShoppingListsResponse
import gq.kirmanak.mealient.datasource.models.GetUnitsResponse
import gq.kirmanak.mealient.datasource.models.GetUserInfoResponse
import gq.kirmanak.mealient.datasource.models.ParseRecipeURLRequest
import gq.kirmanak.mealient.datasource.models.ShoppingListItemInfo
import gq.kirmanak.mealient.datasource.models.UpdateRecipeRequest
import gq.kirmanak.mealient.datasource.models.VersionResponse

interface MealieDataSource {

    suspend fun createRecipe(
        recipe: CreateRecipeRequest,
    ): String

    suspend fun updateRecipe(
        slug: String,
        recipe: UpdateRecipeRequest,
    ): GetRecipeResponse

    /**
     * Tries to acquire authentication token using the provided credentials
     */
    suspend fun authenticate(
        username: String,
        password: String,
    ): String

    suspend fun getVersionInfo(): VersionResponse

    suspend fun requestRecipes(
        page: Int,
        perPage: Int,
    ): List<GetRecipeSummaryResponse>

    suspend fun requestRecipeInfo(
        slug: String,
    ): GetRecipeResponse

    suspend fun parseRecipeFromURL(
        request: ParseRecipeURLRequest,
    ): String

    suspend fun createApiToken(
        request: CreateApiTokenRequest,
    ): CreateApiTokenResponse

    suspend fun requestUserInfo(): GetUserInfoResponse

    suspend fun removeFavoriteRecipe(userId: String, recipeSlug: String)

    suspend fun addFavoriteRecipe(userId: String, recipeSlug: String)

    suspend fun deleteRecipe(slug: String)

    suspend fun getShoppingLists(page: Int, perPage: Int): GetShoppingListsResponse

    suspend fun getShoppingList(id: String): GetShoppingListResponse

    suspend fun deleteShoppingListItem(id: String)

    suspend fun updateShoppingListItem(item: ShoppingListItemInfo)

    suspend fun getFoods(): GetFoodsResponse

    suspend fun getUnits(): GetUnitsResponse

    suspend fun addShoppingListItem(request: CreateShoppingListItemRequest)
}