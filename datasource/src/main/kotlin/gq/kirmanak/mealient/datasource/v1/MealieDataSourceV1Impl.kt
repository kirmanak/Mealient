package gq.kirmanak.mealient.datasource.v1

import gq.kirmanak.mealient.datasource.NetworkError
import gq.kirmanak.mealient.datasource.NetworkRequestWrapper
import gq.kirmanak.mealient.datasource.v0.models.AddRecipeRequestV0
import gq.kirmanak.mealient.datasource.v1.models.GetRecipeResponseV1
import gq.kirmanak.mealient.datasource.v1.models.GetRecipeSummaryResponseV1
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

    override suspend fun addRecipe(
        baseUrl: String,
        token: String?,
        recipe: AddRecipeRequestV0
    ): String {
        TODO("Not yet implemented")
    }

    override suspend fun authenticate(baseUrl: String, username: String, password: String): String {
        TODO("Not yet implemented")
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
        token: String?,
        page: Int,
        perPage: Int
    ): List<GetRecipeSummaryResponseV1> = networkRequestWrapper.makeCallAndHandleUnauthorized(
        block = { service.getRecipeSummary("$baseUrl/api/recipes", token, page, perPage) },
        logMethod = { "requestRecipesV1" },
        logParameters = { "baseUrl = $baseUrl, token = $token, page = $page, perPage = $perPage" }
    ).items

    override suspend fun requestRecipeInfo(
        baseUrl: String,
        token: String?,
        slug: String
    ): GetRecipeResponseV1 = networkRequestWrapper.makeCallAndHandleUnauthorized(
        block = { service.getRecipe("$baseUrl/api/recipes/$slug", token) },
        logMethod = { "requestRecipeInfo" },
        logParameters = { "baseUrl = $baseUrl, token = $token, slug = $slug" }
    )

}

