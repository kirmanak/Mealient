package gq.kirmanak.mealient.datasource.v0

import gq.kirmanak.mealient.datasource.NetworkError
import gq.kirmanak.mealient.datasource.runCatchingExceptCancel
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
    private val logger: Logger,
    private val mealieServiceV0: MealieServiceV0,
    private val json: Json,
) : MealieDataSourceV0 {

    override suspend fun addRecipe(
        baseUrl: String, token: String?, recipe: AddRecipeRequestV0
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
        val errorDetailV0 = errorBody.decode<ErrorDetailV0>()
        throw if (errorDetailV0.detail == "Unauthorized") NetworkError.Unauthorized(it) else it
    }

    override suspend fun getVersionInfo(baseUrl: String): VersionResponseV0 = makeCall(
        block = { getVersion("$baseUrl/api/debug/version") },
        logMethod = { "getVersionInfo" },
        logParameters = { "baseUrl = $baseUrl" },
    ).getOrElse {
        when (it) {
            is HttpException, is SerializationException -> throw NetworkError.NotMealie(it)
            is SocketTimeoutException, is ConnectException -> throw NetworkError.NoServerConnection(
                it
            )
            else -> throw NetworkError.MalformedUrl(it)
        }
    }

    override suspend fun requestRecipes(
        baseUrl: String, token: String?, start: Int, limit: Int
    ): List<GetRecipeSummaryResponseV0> = makeCall(
        block = { getRecipeSummary("$baseUrl/api/recipes/summary", token, start, limit) },
        logMethod = { "requestRecipes" },
        logParameters = { "baseUrl = $baseUrl, token = $token, start = $start, limit = $limit" }
    ).getOrElse {
        val code = (it as? HttpException)?.code() ?: throw it
        if (code == 404) throw NetworkError.NotMealie(it) else throw it
    }

    override suspend fun requestRecipeInfo(
        baseUrl: String, token: String?, slug: String
    ): GetRecipeResponseV0 = makeCall(
        block = { getRecipe("$baseUrl/api/recipes/$slug", token) },
        logMethod = { "requestRecipeInfo" },
        logParameters = { "baseUrl = $baseUrl, token = $token, slug = $slug" }
    ).getOrThrowUnauthorized()

    private suspend inline fun <T> makeCall(
        crossinline block: suspend MealieServiceV0.() -> T,
        crossinline logMethod: () -> String,
        crossinline logParameters: () -> String,
    ): Result<T> {
        logger.v { "${logMethod()} called with: ${logParameters()}" }
        return runCatchingExceptCancel { mealieServiceV0.block() }
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