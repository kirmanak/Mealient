package gq.kirmanak.mealient.datasource.v0

import gq.kirmanak.mealient.datasource.NetworkError
import gq.kirmanak.mealient.datasource.NetworkRequestWrapper
import gq.kirmanak.mealient.datasource.decode
import gq.kirmanak.mealient.datasource.v0.models.AddRecipeRequestV0
import gq.kirmanak.mealient.datasource.v0.models.CreateApiTokenRequestV0
import gq.kirmanak.mealient.datasource.v0.models.CreateApiTokenResponseV0
import gq.kirmanak.mealient.datasource.v0.models.ErrorDetailV0
import gq.kirmanak.mealient.datasource.v0.models.GetRecipeResponseV0
import gq.kirmanak.mealient.datasource.v0.models.GetRecipeSummaryResponseV0
import gq.kirmanak.mealient.datasource.v0.models.GetUserInfoResponseV0
import gq.kirmanak.mealient.datasource.v0.models.ParseRecipeURLRequestV0
import gq.kirmanak.mealient.datasource.v0.models.VersionResponseV0
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject

class MealieDataSourceV0Impl @Inject constructor(
    private val networkRequestWrapper: NetworkRequestWrapper,
    private val service: MealieServiceV0,
    private val json: Json,
) : MealieDataSourceV0 {

    override suspend fun addRecipe(
        recipe: AddRecipeRequestV0,
    ): String = networkRequestWrapper.makeCallAndHandleUnauthorized(
        block = { service.addRecipe(recipe) },
        logMethod = { "addRecipe" },
        logParameters = { "recipe = $recipe" }
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
        val errorDetailV0 = errorBody.decode<ErrorDetailV0>(json)
        throw if (errorDetailV0.detail == "Unauthorized") NetworkError.Unauthorized(it) else it
    }

    override suspend fun getVersionInfo(): VersionResponseV0 = networkRequestWrapper.makeCall(
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
        start: Int,
        limit: Int,
    ): List<GetRecipeSummaryResponseV0> = networkRequestWrapper.makeCallAndHandleUnauthorized(
        block = { service.getRecipeSummary(start, limit) },
        logMethod = { "requestRecipes" },
        logParameters = { "start = $start, limit = $limit" }
    )

    override suspend fun requestRecipeInfo(
        slug: String,
    ): GetRecipeResponseV0 = networkRequestWrapper.makeCallAndHandleUnauthorized(
        block = { service.getRecipe(slug) },
        logMethod = { "requestRecipeInfo" },
        logParameters = { "slug = $slug" }
    )

    override suspend fun parseRecipeFromURL(
        request: ParseRecipeURLRequestV0
    ): String = networkRequestWrapper.makeCallAndHandleUnauthorized(
        block = { service.createRecipeFromURL(request) },
        logMethod = { "parseRecipeFromURL" },
        logParameters = { "request = $request" },
    )

    override suspend fun createApiToken(
        request: CreateApiTokenRequestV0,
    ): CreateApiTokenResponseV0 = networkRequestWrapper.makeCallAndHandleUnauthorized(
        block = { service.createApiToken(request) },
        logMethod = { "createApiToken" },
        logParameters = { "request = $request" }
    )

    override suspend fun requestUserInfo(): GetUserInfoResponseV0 {
        return networkRequestWrapper.makeCallAndHandleUnauthorized(
            block = { service.getUserSelfInfo() },
            logMethod = { "requestUserInfo" },
        )
    }

    override suspend fun removeFavoriteRecipe(
        userId: Int,
        recipeSlug: String
    ): Unit = networkRequestWrapper.makeCallAndHandleUnauthorized(
        block = { service.removeFavoriteRecipe(userId, recipeSlug) },
        logMethod = { "removeFavoriteRecipe" },
        logParameters = { "userId = $userId, recipeSlug = $recipeSlug" }
    )

    override suspend fun addFavoriteRecipe(
        userId: Int,
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
}
