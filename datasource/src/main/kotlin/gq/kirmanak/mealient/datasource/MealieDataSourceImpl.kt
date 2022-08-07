package gq.kirmanak.mealient.datasource

import gq.kirmanak.mealient.datasource.models.*
import gq.kirmanak.mealient.logging.Logger
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import okhttp3.ResponseBody
import retrofit2.HttpException
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
    ): String = makeCall(
        block = { addRecipe("$baseUrl/api/recipes/create", token, recipe) },
        logMethod = { "addRecipe" },
        logParameters = { "baseUrl = $baseUrl, token = $token, recipe = $recipe" }
    ).getOrThrowUnauthorized()

    override suspend fun authenticate(
        baseUrl: String, username: String, password: String
    ): String = makeCall(
        block = { getToken("$baseUrl/api/auth/token", username, password) },
        logMethod = { "authenticate" },
        logParameters = { "baseUrl = $baseUrl, username = $username, password = $password" }
    ).map { it.accessToken }.getOrElse {
        val errorBody = (it as? HttpException)?.response()?.errorBody() ?: throw it
        val errorDetail = errorBody.decode<ErrorDetail>()
        throw if (errorDetail.detail == "Unauthorized") NetworkError.Unauthorized(it) else it
    }

    override suspend fun getVersionInfo(baseUrl: String): VersionResponse = makeCall(
        block = { getVersion("$baseUrl/api/debug/version") },
        logMethod = { "getVersionInfo" },
        logParameters = { "baseUrl = $baseUrl" },
    ).getOrElse {
        throw when (it) {
            is HttpException, is SerializationException -> NetworkError.NotMealie(it)
            else -> NetworkError.MalformedUrl(it)
        }
    }

    override suspend fun requestRecipes(
        baseUrl: String, token: String?, start: Int, limit: Int
    ): List<GetRecipeSummaryResponse> =
        makeCall(
            block = { getRecipeSummary("$baseUrl/api/recipes/summary", token, start, limit) },
            logMethod = { "requestRecipes" },
            logParameters = { "baseUrl = $baseUrl, token = $token, start = $start, limit = $limit" }
        ).getOrThrowUnauthorized()

    override suspend fun requestRecipeInfo(
        baseUrl: String, token: String?, slug: String
    ): GetRecipeResponse = makeCall(
        block = { getRecipe("$baseUrl/api/recipes/$slug", token) },
        logMethod = { "requestRecipeInfo" },
        logParameters = { "baseUrl = $baseUrl, token = $token, slug = $slug" }
    ).getOrThrowUnauthorized()

    private suspend inline fun <T> makeCall(
        crossinline block: suspend MealieService.() -> T,
        crossinline logMethod: () -> String,
        crossinline logParameters: () -> String,
    ): Result<T> {
        logger.v { "${logMethod()} called with: ${logParameters()}" }
        return mealieService.runCatching { block() }
            .onFailure { logger.e(it) { "${logMethod()} request failed with: ${logParameters()}" } }
            .onSuccess { logger.d { "${logMethod()} request succeeded with ${logParameters()}" } }
    }

    @OptIn(ExperimentalSerializationApi::class)
    private inline fun <reified R> ResponseBody.decode(): R = json.decodeFromStream(byteStream())
}

private fun <T> Result<T>.getOrThrowUnauthorized(): T = getOrElse {
    throw if (it is HttpException && it.code() in listOf(401, 403)) {
        NetworkError.Unauthorized(it)
    } else {
        it
    }
}