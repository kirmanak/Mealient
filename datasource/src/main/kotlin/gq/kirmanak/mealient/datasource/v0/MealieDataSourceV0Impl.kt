package gq.kirmanak.mealient.datasource.v0

import gq.kirmanak.mealient.datasource.NetworkError
import gq.kirmanak.mealient.datasource.NetworkRequestWrapper
import gq.kirmanak.mealient.datasource.v0.models.*
import gq.kirmanak.mealient.logging.Logger
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MealieDataSourceV0Impl @Inject constructor(
    private val networkRequestWrapper: NetworkRequestWrapper,
    private val logger: Logger,
    private val service: MealieServiceV0,
    private val json: Json,
) : MealieDataSourceV0 {

    override suspend fun addRecipe(
        baseUrl: String,
        token: String?,
        recipe: AddRecipeRequestV0,
    ): String = networkRequestWrapper.makeCallAndHandleUnauthorized(
        block = { service.addRecipe("$baseUrl/api/recipes/create", token, recipe) },
        logMethod = { "addRecipe" },
        logParameters = { "baseUrl = $baseUrl, token = $token, recipe = $recipe" }
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
        val errorDetailV0 = errorBody.decode<ErrorDetailV0>()
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
        token: String?,
        start: Int,
        limit: Int,
    ): List<GetRecipeSummaryResponseV0> = networkRequestWrapper.makeCallAndHandleUnauthorized(
        block = { service.getRecipeSummary("$baseUrl/api/recipes/summary", token, start, limit) },
        logMethod = { "requestRecipes" },
        logParameters = { "baseUrl = $baseUrl, token = $token, start = $start, limit = $limit" }
    )

    override suspend fun requestRecipeInfo(
        baseUrl: String,
        token: String?,
        slug: String,
    ): GetRecipeResponseV0 = networkRequestWrapper.makeCallAndHandleUnauthorized(
        block = { service.getRecipe("$baseUrl/api/recipes/$slug", token) },
        logMethod = { "requestRecipeInfo" },
        logParameters = { "baseUrl = $baseUrl, token = $token, slug = $slug" }
    )

    @OptIn(ExperimentalSerializationApi::class)
    private inline fun <reified R> ResponseBody.decode(): R = json.decodeFromStream(byteStream())
}
