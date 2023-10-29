package gq.kirmanak.mealient.datasource.impl

import gq.kirmanak.mealient.datasource.MealieDataSource
import gq.kirmanak.mealient.datasource.MealieService
import gq.kirmanak.mealient.datasource.NetworkError
import gq.kirmanak.mealient.datasource.NetworkRequestWrapper
import gq.kirmanak.mealient.datasource.models.CreateApiTokenRequest
import gq.kirmanak.mealient.datasource.models.CreateApiTokenResponse
import gq.kirmanak.mealient.datasource.models.CreateRecipeRequest
import gq.kirmanak.mealient.datasource.models.CreateShoppingListItemRequest
import gq.kirmanak.mealient.datasource.models.ErrorDetail
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
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import java.net.SocketException
import java.net.SocketTimeoutException
import javax.inject.Inject

class MealieDataSourceImpl @Inject constructor(
    private val networkRequestWrapper: NetworkRequestWrapper,
    private val service: MealieService,
) : MealieDataSource {

    override suspend fun createRecipe(
        recipe: CreateRecipeRequest
    ): String = networkRequestWrapper.makeCallAndHandleUnauthorized(
        block = { service.createRecipe(recipe) },
        logMethod = { "createRecipe" },
        logParameters = { "recipe = $recipe" }
    ).trim('"')

    override suspend fun updateRecipe(
        slug: String,
        recipe: UpdateRecipeRequest
    ): GetRecipeResponse = networkRequestWrapper.makeCallAndHandleUnauthorized(
        block = { service.updateRecipe(recipe, slug) },
        logMethod = { "updateRecipe" },
        logParameters = { "slug = $slug, recipe = $recipe" }
    )

    override suspend fun authenticate(
        username: String,
        password: String,
    ): String = networkRequestWrapper.makeCall(
        block = { service.getToken(username, password) },
        logMethod = { "authenticate" },
        logParameters = { "username = $username, password = $password" }
    ).map { it.accessToken }.getOrElse {
        val errorDetail = (it as? ResponseException)?.response?.body<ErrorDetail>() ?: throw it
        throw if (errorDetail.detail == "Unauthorized") NetworkError.Unauthorized(it) else it
    }

    override suspend fun getVersionInfo(): VersionResponse = networkRequestWrapper.makeCall(
        block = { service.getVersion() },
        logMethod = { "getVersionInfo" },
    ).getOrElse {
        throw when (it) {
            is ResponseException, is NoTransformationFoundException -> NetworkError.NotMealie(it)
            is SocketTimeoutException, is SocketException -> NetworkError.NoServerConnection(it)
            else -> NetworkError.MalformedUrl(it)
        }
    }

    override suspend fun requestRecipes(
        page: Int,
        perPage: Int
    ): List<GetRecipeSummaryResponse> = networkRequestWrapper.makeCallAndHandleUnauthorized(
        block = { service.getRecipeSummary(page, perPage) },
        logMethod = { "requestRecipes" },
        logParameters = { "page = $page, perPage = $perPage" }
    ).items

    override suspend fun requestRecipeInfo(
        slug: String
    ): GetRecipeResponse = networkRequestWrapper.makeCallAndHandleUnauthorized(
        block = { service.getRecipe(slug) },
        logMethod = { "requestRecipeInfo" },
        logParameters = { "slug = $slug" }
    )

    override suspend fun parseRecipeFromURL(
        request: ParseRecipeURLRequest
    ): String = networkRequestWrapper.makeCallAndHandleUnauthorized(
        block = { service.createRecipeFromURL(request) },
        logMethod = { "parseRecipeFromURL" },
        logParameters = { "request = $request" }
    )

    override suspend fun createApiToken(
        request: CreateApiTokenRequest
    ): CreateApiTokenResponse = networkRequestWrapper.makeCallAndHandleUnauthorized(
        block = { service.createApiToken(request) },
        logMethod = { "createApiToken" },
        logParameters = { "request = $request" }
    )

    override suspend fun requestUserInfo(): GetUserInfoResponse {
        return networkRequestWrapper.makeCallAndHandleUnauthorized(
            block = { service.getUserSelfInfo() },
            logMethod = { "requestUserInfo" },
        )
    }

    override suspend fun removeFavoriteRecipe(
        userId: String,
        recipeSlug: String
    ): Unit = networkRequestWrapper.makeCallAndHandleUnauthorized(
        block = { service.removeFavoriteRecipe(userId, recipeSlug) },
        logMethod = { "removeFavoriteRecipe" },
        logParameters = { "userId = $userId, recipeSlug = $recipeSlug" }
    )

    override suspend fun addFavoriteRecipe(
        userId: String,
        recipeSlug: String
    ): Unit = networkRequestWrapper.makeCallAndHandleUnauthorized(
        block = { service.addFavoriteRecipe(userId, recipeSlug) },
        logMethod = { "addFavoriteRecipe" },
        logParameters = { "userId = $userId, recipeSlug = $recipeSlug" }
    )

    override suspend fun deleteRecipe(
        slug: String
    ): Unit = networkRequestWrapper.makeCallAndHandleUnauthorized(
        block = { service.deleteRecipe(slug) },
        logMethod = { "deleteRecipe" },
        logParameters = { "slug = $slug" }
    )

    override suspend fun getShoppingLists(
        page: Int,
        perPage: Int,
    ): GetShoppingListsResponse = networkRequestWrapper.makeCallAndHandleUnauthorized(
        block = { service.getShoppingLists(page, perPage) },
        logMethod = { "getShoppingLists" },
        logParameters = { "page = $page, perPage = $perPage" }
    )

    override suspend fun getShoppingList(
        id: String
    ): GetShoppingListResponse = networkRequestWrapper.makeCallAndHandleUnauthorized(
        block = { service.getShoppingList(id) },
        logMethod = { "getShoppingList" },
        logParameters = { "id = $id" }
    )

    private suspend fun getShoppingListItem(
        id: String,
    ): JsonElement = networkRequestWrapper.makeCallAndHandleUnauthorized(
        block = { service.getShoppingListItem(id) },
        logMethod = { "getShoppingListItem" },
        logParameters = { "id = $id" }
    )

    private suspend fun updateShoppingListItem(
        id: String,
        request: JsonElement,
    ) = networkRequestWrapper.makeCallAndHandleUnauthorized(
        block = { service.updateShoppingListItem(id, request) },
        logMethod = { "updateShoppingListItem" },
        logParameters = { "id = $id, request = $request" }
    )

    override suspend fun deleteShoppingListItem(
        id: String,
    ) = networkRequestWrapper.makeCallAndHandleUnauthorized(
        block = { service.deleteShoppingListItem(id) },
        logMethod = { "deleteShoppingListItem" },
        logParameters = { "id = $id" }
    )

    override suspend fun updateShoppingListItem(
        item: ShoppingListItemInfo
    ) {
        // Has to be done in two steps because we can't specify only the changed fields
        val remoteItem = getShoppingListItem(item.id)
        val updatedItem = remoteItem.jsonObject.toMutableMap().apply {
            put("checked", JsonPrimitive(item.checked))
            put("isFood", JsonPrimitive(item.isFood))
            put("note", JsonPrimitive(item.note))
            put("quantity", JsonPrimitive(item.quantity))
            put("foodId", JsonPrimitive(item.food?.id))
            put("unitId", JsonPrimitive(item.unit?.id))
            remove("unit")
            remove("food")
        }
        updateShoppingListItem(item.id, JsonObject(updatedItem))
    }

    override suspend fun getFoods(): GetFoodsResponse {
        return networkRequestWrapper.makeCallAndHandleUnauthorized(
            block = { service.getFoods(perPage = -1) },
            logMethod = { "getFoods" },
        )
    }

    override suspend fun getUnits(): GetUnitsResponse {
        return networkRequestWrapper.makeCallAndHandleUnauthorized(
            block = { service.getUnits(perPage = -1) },
            logMethod = { "getUnits" },
        )
    }

    override suspend fun addShoppingListItem(
        request: CreateShoppingListItemRequest
    ) = networkRequestWrapper.makeCallAndHandleUnauthorized(
        block = { service.createShoppingListItem(request) },
        logMethod = { "addShoppingListItem" },
        logParameters = { "request = $request" }
    )
}
