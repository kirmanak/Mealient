package gq.kirmanak.mealient.datasource.v0

import gq.kirmanak.mealient.datasource.NetworkError
import gq.kirmanak.mealient.datasource.NetworkRequestWrapper
import gq.kirmanak.mealient.datasource.decode
import gq.kirmanak.mealient.datasource.v0.models.AddRecipeRequestV0
import gq.kirmanak.mealient.datasource.v0.models.CreateApiTokenRequestV0
import gq.kirmanak.mealient.datasource.v0.models.ErrorDetailV0
import gq.kirmanak.mealient.datasource.v0.models.GetRecipeResponseV0
import gq.kirmanak.mealient.datasource.v0.models.GetRecipeSummaryResponseV0
import gq.kirmanak.mealient.datasource.v0.models.ParseRecipeURLRequestV0
import gq.kirmanak.mealient.datasource.v0.models.VersionResponseV0
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MealieDataSourceV0Impl @Inject constructor(
    private val networkRequestWrapper: NetworkRequestWrapper,
    private val service: MealieServiceV0,
    private val json: Json,
) : MealieDataSourceV0 {

    override suspend fun addRecipe(
        baseUrl: String,
        recipe: AddRecipeRequestV0,
    ): String = networkRequestWrapper.makeCallAndHandleUnauthorized(
        block = { service.addRecipe("$baseUrl/api/recipes/create", recipe) },
        logMethod = { "addRecipe" },
        logParameters = { "baseUrl = $baseUrl, recipe = $recipe" }
    )

    override suspend fun authenticate(
        baseUrl: String,
        username: String,
        password: String,
    ): String = networkRequestWrapper.makeCall(
        block = { service.getToken("$baseUrl/api/auth/token", username, password) },
        logMethod = { "authenticate" },
        logParameters = { "baseUrl = $baseUrl, username = $username, password = $password" }
    ).map { it.accessToken }.getOrElse {
        val errorBody = (it as? HttpException)?.response()?.errorBody() ?: throw it
        val errorDetailV0 = errorBody.decode<ErrorDetailV0>(json)
        throw if (errorDetailV0.detail == "Unauthorized") NetworkError.Unauthorized(it) else it
    }

    override suspend fun getVersionInfo(
        baseUrl: String
    ): VersionResponseV0 = networkRequestWrapper.makeCall(
        block = { service.getVersion("$baseUrl/api/debug/version") },
        logMethod = { "getVersionInfo" },
        logParameters = { "baseUrl = $baseUrl" },
    ).getOrElse {
        throw when (it) {
            is HttpException, is SerializationException -> NetworkError.NotMealie(it)
            is SocketTimeoutException, is ConnectException -> NetworkError.NoServerConnection(it)
            else -> NetworkError.MalformedUrl(it)
        }
    }

    override suspend fun requestRecipes(
        baseUrl: String,
        start: Int,
        limit: Int,
    ): List<GetRecipeSummaryResponseV0> = networkRequestWrapper.makeCallAndHandleUnauthorized(
        block = { service.getRecipeSummary("$baseUrl/api/recipes/summary", start, limit) },
        logMethod = { "requestRecipes" },
        logParameters = { "baseUrl = $baseUrl, start = $start, limit = $limit" }
    )

    override suspend fun requestRecipeInfo(
        baseUrl: String,
        slug: String,
    ): GetRecipeResponseV0 = networkRequestWrapper.makeCallAndHandleUnauthorized(
        block = { service.getRecipe("$baseUrl/api/recipes/$slug") },
        logMethod = { "requestRecipeInfo" },
        logParameters = { "baseUrl = $baseUrl, slug = $slug" }
    )

    override suspend fun parseRecipeFromURL(
        baseUrl: String,
        request: ParseRecipeURLRequestV0
    ): String = networkRequestWrapper.makeCallAndHandleUnauthorized(
        block = { service.createRecipeFromURL("$baseUrl/api/recipes/create-url", request) },
        logMethod = { "parseRecipeFromURL" },
        logParameters = { "baseUrl = $baseUrl, request = $request" },
    )

    override suspend fun createApiToken(
        baseUrl: String,
        request: CreateApiTokenRequestV0,
    ): String = networkRequestWrapper.makeCallAndHandleUnauthorized(
        block = { service.createApiToken("$baseUrl/api/users/api-tokens", request) },
        logMethod = { "createApiToken" },
        logParameters = { "baseUrl = $baseUrl, request = $request" }
    )
}
