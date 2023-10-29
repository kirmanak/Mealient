package gq.kirmanak.mealient.datasource.v1

import gq.kirmanak.mealient.datasource.NetworkError
import gq.kirmanak.mealient.datasource.NetworkRequestWrapper
import gq.kirmanak.mealient.datasource.decode
import gq.kirmanak.mealient.datasource.models.ShoppingListItemInfo
import gq.kirmanak.mealient.datasource.v1.models.CreateApiTokenRequestV1
import gq.kirmanak.mealient.datasource.v1.models.CreateApiTokenResponseV1
import gq.kirmanak.mealient.datasource.v1.models.CreateRecipeRequestV1
import gq.kirmanak.mealient.datasource.v1.models.CreateShoppingListItemRequestV1
import gq.kirmanak.mealient.datasource.v1.models.ErrorDetailV1
import gq.kirmanak.mealient.datasource.v1.models.GetFoodsResponseV1
import gq.kirmanak.mealient.datasource.v1.models.GetRecipeResponseV1
import gq.kirmanak.mealient.datasource.v1.models.GetRecipeSummaryResponseV1
import gq.kirmanak.mealient.datasource.v1.models.GetShoppingListResponseV1
import gq.kirmanak.mealient.datasource.v1.models.GetShoppingListsResponseV1
import gq.kirmanak.mealient.datasource.v1.models.GetUnitsResponseV1
import gq.kirmanak.mealient.datasource.v1.models.GetUserInfoResponseV1
import gq.kirmanak.mealient.datasource.v1.models.ParseRecipeURLRequestV1
import gq.kirmanak.mealient.datasource.v1.models.UpdateRecipeRequestV1
import gq.kirmanak.mealient.datasource.v1.models.VersionResponseV1
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject

class MealieDataSourceV1Impl @Inject constructor(
    private val networkRequestWrapper: NetworkRequestWrapper,
    private val service: MealieServiceV1,
    private val json: Json,
) : MealieDataSourceV1 {

    override suspend fun createRecipe(
        recipe: CreateRecipeRequestV1
    ): String = networkRequestWrapper.makeCallAndHandleUnauthorized(
        block = { service.createRecipe(recipe) },
        logMethod = { "createRecipe" },
        logParameters = { "recipe = $recipe" }
    ).trim('"')

    override suspend fun updateRecipe(
        slug: String,
        recipe: UpdateRecipeRequestV1
    ): GetRecipeResponseV1 = networkRequestWrapper.makeCallAndHandleUnauthorized(
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
        val errorBody = (it as? HttpException)?.response()?.errorBody() ?: throw it
        val errorDetailV0 = errorBody.decode<ErrorDetailV1>(json)
        throw if (errorDetailV0.detail == "Unauthorized") NetworkError.Unauthorized(it) else it
    }

    override suspend fun getVersionInfo(): VersionResponseV1 = networkRequestWrapper.makeCall(
        block = { service.getVersion() },
        logMethod = { "getVersionInfo" },
    ).getOrElse {
        throw when (it) {
            is HttpException, is SerializationException -> NetworkError.NotMealie(it)
            is SocketTimeoutException, is ConnectException -> NetworkError.NoServerConnection(it)
            else -> NetworkError.MalformedUrl(it)
        }
    }

    override suspend fun requestRecipes(
        page: Int,
        perPage: Int
    ): List<GetRecipeSummaryResponseV1> = networkRequestWrapper.makeCallAndHandleUnauthorized(
        block = { service.getRecipeSummary(page, perPage) },
        logMethod = { "requestRecipes" },
        logParameters = { "page = $page, perPage = $perPage" }
    ).items

    override suspend fun requestRecipeInfo(
        slug: String
    ): GetRecipeResponseV1 = networkRequestWrapper.makeCallAndHandleUnauthorized(
        block = { service.getRecipe(slug) },
        logMethod = { "requestRecipeInfo" },
        logParameters = { "slug = $slug" }
    )

    override suspend fun parseRecipeFromURL(
        request: ParseRecipeURLRequestV1
    ): String = networkRequestWrapper.makeCallAndHandleUnauthorized(
        block = { service.createRecipeFromURL(request) },
        logMethod = { "parseRecipeFromURL" },
        logParameters = { "request = $request" }
    )

    override suspend fun createApiToken(
        request: CreateApiTokenRequestV1
    ): CreateApiTokenResponseV1 = networkRequestWrapper.makeCallAndHandleUnauthorized(
        block = { service.createApiToken(request) },
        logMethod = { "createApiToken" },
        logParameters = { "request = $request" }
    )

    override suspend fun requestUserInfo(): GetUserInfoResponseV1 {
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
    ): GetShoppingListsResponseV1 = networkRequestWrapper.makeCallAndHandleUnauthorized(
        block = { service.getShoppingLists(page, perPage) },
        logMethod = { "getShoppingLists" },
        logParameters = { "page = $page, perPage = $perPage" }
    )

    override suspend fun getShoppingList(
        id: String
    ): GetShoppingListResponseV1 = networkRequestWrapper.makeCallAndHandleUnauthorized(
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

    override suspend fun getFoods(): GetFoodsResponseV1 {
        return networkRequestWrapper.makeCallAndHandleUnauthorized(
            block = { service.getFoods(perPage = -1) },
            logMethod = { "getFoods" },
        )
    }

    override suspend fun getUnits(): GetUnitsResponseV1 {
        return networkRequestWrapper.makeCallAndHandleUnauthorized(
            block = { service.getUnits(perPage = -1) },
            logMethod = { "getUnits" },
        )
    }

    override suspend fun addShoppingListItem(
        request: CreateShoppingListItemRequestV1
    ) = networkRequestWrapper.makeCallAndHandleUnauthorized(
        block = { service.createShoppingListItem(request) },
        logMethod = { "addShoppingListItem" },
        logParameters = { "request = $request" }
    )
}
