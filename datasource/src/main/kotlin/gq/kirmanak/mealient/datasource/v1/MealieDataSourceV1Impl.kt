package gq.kirmanak.mealient.datasource.v1

import gq.kirmanak.mealient.datasource.NetworkError
import gq.kirmanak.mealient.datasource.NetworkRequestWrapper
import gq.kirmanak.mealient.datasource.decode
import gq.kirmanak.mealient.datasource.v1.models.CreateRecipeRequestV1
import gq.kirmanak.mealient.datasource.v1.models.ErrorDetailV1
import gq.kirmanak.mealient.datasource.v1.models.GetRecipeResponseV1
import gq.kirmanak.mealient.datasource.v1.models.GetRecipeSummaryResponseV1
import gq.kirmanak.mealient.datasource.v1.models.ParseRecipeURLRequestV1
import gq.kirmanak.mealient.datasource.v1.models.UpdateRecipeRequestV1
import gq.kirmanak.mealient.datasource.v1.models.VersionResponseV1
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MealieDataSourceV1Impl @Inject constructor(
    private val networkRequestWrapper: NetworkRequestWrapper,
    private val service: MealieServiceV1,
    private val json: Json,
) : MealieDataSourceV1 {

    override suspend fun createRecipe(
        baseUrl: String,
        recipe: CreateRecipeRequestV1
    ): String = networkRequestWrapper.makeCallAndHandleUnauthorized(
        block = { service.createRecipe("$baseUrl/api/recipes", recipe) },
        logMethod = { "createRecipe" },
        logParameters = { "baseUrl = $baseUrl, recipe = $recipe" }
    )

    override suspend fun updateRecipe(
        baseUrl: String,
        slug: String,
        recipe: UpdateRecipeRequestV1
    ): GetRecipeResponseV1 = networkRequestWrapper.makeCallAndHandleUnauthorized(
        block = { service.updateRecipe("$baseUrl/api/recipes/$slug", recipe) },
        logMethod = { "updateRecipe" },
        logParameters = { "baseUrl = $baseUrl, slug = $slug, recipe = $recipe" }
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
        val errorDetailV0 = errorBody.decode<ErrorDetailV1>(json)
        throw if (errorDetailV0.detail == "Unauthorized") NetworkError.Unauthorized(it) else it
    }

    override suspend fun getVersionInfo(
        baseUrl: String,
    ): VersionResponseV1 = networkRequestWrapper.makeCall(
        block = { service.getVersion("$baseUrl/api/app/about") },
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
        page: Int,
        perPage: Int
    ): List<GetRecipeSummaryResponseV1> = networkRequestWrapper.makeCallAndHandleUnauthorized(
        block = { service.getRecipeSummary("$baseUrl/api/recipes", page, perPage) },
        logMethod = { "requestRecipes" },
        logParameters = { "baseUrl = $baseUrl, page = $page, perPage = $perPage" }
    ).items

    override suspend fun requestRecipeInfo(
        baseUrl: String,
        slug: String
    ): GetRecipeResponseV1 = networkRequestWrapper.makeCallAndHandleUnauthorized(
        block = { service.getRecipe("$baseUrl/api/recipes/$slug") },
        logMethod = { "requestRecipeInfo" },
        logParameters = { "baseUrl = $baseUrl, slug = $slug" }
    )

    override suspend fun parseRecipeFromURL(
        baseUrl: String,
        request: ParseRecipeURLRequestV1
    ): String = networkRequestWrapper.makeCallAndHandleUnauthorized(
        block = { service.createRecipeFromURL("$baseUrl/api/recipes/create-url", request) },
        logMethod = { "parseRecipeFromURL" },
        logParameters = { "baseUrl = $baseUrl, request = $request" }

    )

}

