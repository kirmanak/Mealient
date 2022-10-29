package gq.kirmanak.mealient.datasource.v1

import gq.kirmanak.mealient.datasource.models.AddRecipeRequest
import gq.kirmanak.mealient.datasource.models.GetRecipeResponse
import gq.kirmanak.mealient.datasource.models.NetworkError
import gq.kirmanak.mealient.datasource.models.VersionResponse
import gq.kirmanak.mealient.datasource.v1.models.GetRecipeSummaryResponseV1
import gq.kirmanak.mealient.logging.Logger
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MealieDataSourceV1Impl @Inject constructor(
    private val logger: Logger,
    private val mealieService: MealieServiceV1,
    private val json: Json,
) : MealieDataSourceV1 {

    override suspend fun addRecipe(
        baseUrl: String,
        token: String?,
        recipe: AddRecipeRequest
    ): String {
        TODO("Not yet implemented")
    }

    override suspend fun authenticate(baseUrl: String, username: String, password: String): String {
        TODO("Not yet implemented")
    }

    override suspend fun getVersionInfo(baseUrl: String): VersionResponse = makeCall(
        block = { getVersion("$baseUrl/api/app/about") },
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
        limit: Int
    ): List<GetRecipeSummaryResponseV1> {
        // Imagine start is 30 and limit is 15. It means that we already have page 1 and 2, now we need page 3
        val perPage = limit
        val page = start / perPage + 1
        return makeCall(
            block = { getRecipeSummary("$baseUrl/api/recipes", token, page, perPage) },
            logMethod = { "requestRecipesV1" },
            logParameters = { "baseUrl = $baseUrl, token = $token, start = $start, limit = $limit" }
        ).map { it.items }.getOrThrowUnauthorized()
    }

    override suspend fun requestRecipeInfo(
        baseUrl: String,
        token: String?,
        slug: String
    ): GetRecipeResponse {
        TODO("Not yet implemented")
    }

    private suspend inline fun <T> makeCall(
        crossinline block: suspend MealieServiceV1.() -> T,
        crossinline logMethod: () -> String,
        crossinline logParameters: () -> String,
    ): Result<T> {
        logger.v { "${logMethod()} called with: ${logParameters()}" }
        return mealieService.runCatching { block() }
            .onFailure { logger.e(it) { "${logMethod()} request failed with: ${logParameters()}" } }
            .onSuccess { logger.d { "${logMethod()} request succeeded with ${logParameters()}" } }
    }
}

private fun <T> Result<T>.getOrThrowUnauthorized(): T = getOrElse {
    throw if (it is HttpException && it.code() in listOf(401, 403)) {
        NetworkError.Unauthorized(it)
    } else {
        it
    }
}
