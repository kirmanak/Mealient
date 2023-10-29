package gq.kirmanak.mealient.datasource.v1

import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import gq.kirmanak.mealient.datasource.ServerUrlProvider
import gq.kirmanak.mealient.datasource.v1.models.CreateApiTokenRequestV1
import gq.kirmanak.mealient.datasource.v1.models.CreateApiTokenResponseV1
import gq.kirmanak.mealient.datasource.v1.models.CreateRecipeRequestV1
import gq.kirmanak.mealient.datasource.v1.models.CreateShoppingListItemRequestV1
import gq.kirmanak.mealient.datasource.v1.models.GetFoodsResponseV1
import gq.kirmanak.mealient.datasource.v1.models.GetRecipeResponseV1
import gq.kirmanak.mealient.datasource.v1.models.GetRecipesResponseV1
import gq.kirmanak.mealient.datasource.v1.models.GetShoppingListResponseV1
import gq.kirmanak.mealient.datasource.v1.models.GetShoppingListsResponseV1
import gq.kirmanak.mealient.datasource.v1.models.GetTokenResponseV1
import gq.kirmanak.mealient.datasource.v1.models.GetUnitsResponseV1
import gq.kirmanak.mealient.datasource.v1.models.GetUserInfoResponseV1
import gq.kirmanak.mealient.datasource.v1.models.ParseRecipeURLRequestV1
import gq.kirmanak.mealient.datasource.v1.models.UpdateRecipeRequestV1
import gq.kirmanak.mealient.datasource.v1.models.VersionResponseV1
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.URLBuilder
import io.ktor.http.contentType
import io.ktor.http.parameters
import io.ktor.http.path
import io.ktor.http.takeFrom
import kotlinx.serialization.json.JsonElement
import javax.inject.Inject
import javax.inject.Provider

class MealieServiceV1Ktor @Inject constructor(
    private val httpClient: HttpClient,
    private val serverUrlProviderProvider: Provider<ServerUrlProvider>,
) : MealieServiceV1 {

    private val serverUrlProvider: ServerUrlProvider
        get() = serverUrlProviderProvider.get()

    override suspend fun getToken(username: String, password: String): GetTokenResponseV1 {
        val formParameters = parameters {
            append("username", username)
            append("password", password)
        }

        return httpClient.post {
            endpoint("/api/auth/token")
            setBody(FormDataContent(formParameters))
        }.body()
    }

    override suspend fun createRecipe(addRecipeRequest: CreateRecipeRequestV1): String {
        return httpClient.post {
            endpoint("/api/recipes")
            contentType(ContentType.Application.Json)
            setBody(addRecipeRequest)
        }.body()
    }

    override suspend fun updateRecipe(
        addRecipeRequest: UpdateRecipeRequestV1,
        slug: String,
    ): GetRecipeResponseV1 {
        return httpClient.patch {
            endpoint("/api/recipes/$slug")
            contentType(ContentType.Application.Json)
            setBody(addRecipeRequest)
        }.body()
    }

    override suspend fun getVersion(): VersionResponseV1 {
        return httpClient.get {
            endpoint("/api/app/about")
        }.body()
    }

    override suspend fun getRecipeSummary(page: Int, perPage: Int): GetRecipesResponseV1 {
        return httpClient.get {
            endpoint("/api/recipes") {
                parameters.append("page", page.toString())
                parameters.append("perPage", perPage.toString())
            }
        }.body()
    }

    override suspend fun getRecipe(slug: String): GetRecipeResponseV1 {
        return httpClient.get {
            endpoint("/api/recipes/$slug")
        }.body()
    }

    override suspend fun createRecipeFromURL(request: ParseRecipeURLRequestV1): String {
        return httpClient.post {
            endpoint("/api/recipes/create-url")
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    override suspend fun createApiToken(request: CreateApiTokenRequestV1): CreateApiTokenResponseV1 {
        return httpClient.post {
            endpoint("/api/users/api-tokens")
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    override suspend fun getUserSelfInfo(): GetUserInfoResponseV1 {
        return httpClient.get {
            endpoint("/api/users/self")
        }.body()
    }

    override suspend fun removeFavoriteRecipe(userId: String, recipeSlug: String) {
        httpClient.delete {
            endpoint("/api/users/$userId/favorite-recipes/$recipeSlug/remove")
        }
    }

    override suspend fun addFavoriteRecipe(userId: String, recipeSlug: String) {
        httpClient.post {
            endpoint("/api/users/$userId/favorite-recipes/$recipeSlug/add")
        }
    }

    override suspend fun deleteRecipe(slug: String) {
        httpClient.delete {
            endpoint("/api/recipes/$slug")
        }
    }

    override suspend fun getShoppingLists(page: Int, perPage: Int): GetShoppingListsResponseV1 {
        return httpClient.get {
            endpoint("/api/groups/shopping/lists") {
                parameters.append("page", page.toString())
                parameters.append("perPage", perPage.toString())
            }
        }.body()
    }

    override suspend fun getShoppingList(id: String): GetShoppingListResponseV1 {
        return httpClient.get {
            endpoint("/api/groups/shopping/lists/$id")
        }.body()
    }

    override suspend fun getShoppingListItem(id: String): JsonElement {
        return httpClient.get {
            endpoint("/api/groups/shopping/items/$id")
        }.body()
    }

    override suspend fun updateShoppingListItem(id: String, request: JsonElement) {
        httpClient.put {
            endpoint("/api/groups/shopping/items/$id")
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }

    override suspend fun deleteShoppingListItem(id: String) {
        httpClient.delete {
            endpoint("/api/groups/shopping/items/$id")
        }
    }

    override suspend fun getFoods(perPage: Int): GetFoodsResponseV1 {
        return httpClient.get {
            endpoint("/api/foods") {
                parameters.append("perPage", perPage.toString())
            }
        }.body()
    }

    override suspend fun getUnits(perPage: Int): GetUnitsResponseV1 {
        return httpClient.get {
            endpoint("/api/units") {
                parameters.append("perPage", perPage.toString())
            }
        }.body()
    }

    override suspend fun createShoppingListItem(request: CreateShoppingListItemRequestV1) {
        httpClient.post {
            endpoint("/api/groups/shopping/items")
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }

    private suspend fun HttpRequestBuilder.endpoint(
        path: String,
        block: URLBuilder.() -> Unit = {}
    ) {
        val baseUrl = checkNotNull(serverUrlProvider.getUrl()) { "Server URL is not set" }
        url {
            takeFrom(baseUrl)
            path(path)
            block()
        }
    }
}