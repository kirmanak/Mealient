package gq.kirmanak.mealient.datasource

import gq.kirmanak.mealient.datasource.models.*
import gq.kirmanak.mealient.logging.Logger
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MealieDataSourceImpl @Inject constructor(
    private val logger: Logger,
    private val mealieService: MealieService,
    private val json: Json,
) : MealieDataSource {

    override suspend fun addRecipe(
        baseUrl: String, token: String?, recipe: AddRecipeRequest
    ): String {
        logger.v { "addRecipe() called with: baseUrl = $baseUrl, token = $token, recipe = $recipe" }
        return mealieService.runCatching { addRecipe("$baseUrl/api/recipes/create", token, recipe) }
            .onFailure { logger.e(it) { "addRecipe() request failed with: baseUrl = $baseUrl, token = $token, recipe = $recipe" } }
            .onSuccess { logger.d { "addRecipe() request succeeded with: baseUrl = $baseUrl, token = $token, recipe = $recipe" } }
            .getOrThrowUnauthorized()
    }

    override suspend fun authenticate(
        baseUrl: String, username: String, password: String
    ): String {
        logger.v { "authenticate() called with: baseUrl = $baseUrl, username = $username, password = $password" }
        return mealieService.runCatching { getToken("$baseUrl/api/auth/token", username, password) }
            .onFailure { logger.e(it) { "authenticate() request failed with: baseUrl = $baseUrl, username = $username, password = $password" } }
            .onSuccess { logger.d { "authenticate() request succeeded with: baseUrl = $baseUrl, username = $username, password = $password" } }
            .mapCatching { parseToken(it) }
            .getOrThrowUnauthorized()
    }

    override suspend fun getVersionInfo(baseUrl: String): VersionResponse {
        logger.v { "getVersionInfo() called with: baseUrl = $baseUrl" }
        return mealieService.runCatching { getVersion("$baseUrl/api/debug/version") }
            .onFailure { logger.e(it) { "getVersionInfo() request failed with: baseUrl = $baseUrl" } }
            .onSuccess { logger.d { "getVersionInfo() request succeeded with: baseUrl = $baseUrl" } }
            .getOrThrowUnauthorized()
    }

    override suspend fun requestRecipes(
        baseUrl: String, token: String?, start: Int, limit: Int
    ): List<GetRecipeSummaryResponse> {
        logger.v { "requestRecipes() called with: baseUrl = $baseUrl, token = $token, start = $start, limit = $limit" }
        return mealieService.runCatching {
            getRecipeSummary("$baseUrl/api/recipes/summary", token, start, limit)
        }
            .onFailure { logger.e(it) { "requestRecipes() request failed with: baseUrl = $baseUrl, token = $token, start = $start, limit = $limit" } }
            .onSuccess { logger.d { "requestRecipes() request succeeded with: baseUrl = $baseUrl, token = $token, start = $start, limit = $limit" } }
            .getOrThrowUnauthorized()
    }

    override suspend fun requestRecipeInfo(
        baseUrl: String, token: String?, slug: String
    ): GetRecipeResponse {
        logger.v { "requestRecipeInfo() called with: baseUrl = $baseUrl, token = $token, slug = $slug" }
        return mealieService.runCatching { getRecipe("$baseUrl/api/recipes/$slug", token) }
            .onFailure { logger.e(it) { "requestRecipeInfo() request failed with: baseUrl = $baseUrl, token = $token, slug = $slug" } }
            .onSuccess { logger.d { "requestRecipeInfo() request succeeded with: baseUrl = $baseUrl, token = $token, slug = $slug" } }
            .getOrThrowUnauthorized()
    }

    private fun parseToken(
        response: Response<GetTokenResponse>
    ): String = if (response.isSuccessful) {
        response.body()?.accessToken
            ?: throw NetworkError.NotMealie(NullPointerException("Body is null"))
    } else {
        val cause = HttpException(response)
        val errorDetail = json.runCatching<Json, ErrorDetail> { decodeErrorBody(response) }
            .onFailure { logger.e(it) { "Can't decode error body" } }
            .getOrNull()
        throw when (errorDetail?.detail) {
            "Unauthorized" -> NetworkError.Unauthorized(cause)
            else -> NetworkError.NotMealie(cause)
        }
    }
}

private fun <T> Result<T>.getOrThrowUnauthorized(): T = getOrElse {
    throw if (it is HttpException && it.code() in listOf(401, 403)) {
        NetworkError.Unauthorized(it)
    } else {
        it
    }
}

@OptIn(ExperimentalSerializationApi::class)
private inline fun <T, reified R> Json.decodeErrorBody(response: Response<T>): R {
    val responseBody = checkNotNull(response.errorBody()) { "Can't decode absent error body" }
    return decodeFromStream(responseBody.byteStream())
}

fun Throwable.mapToNetworkError(): NetworkError = when (this) {
    is HttpException, is SerializationException -> NetworkError.NotMealie(this)
    else -> NetworkError.NoServerConnection(this)
}