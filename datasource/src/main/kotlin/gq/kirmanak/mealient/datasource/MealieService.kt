package gq.kirmanak.mealient.datasource

import gq.kirmanak.mealient.datasource.DataSourceModule.Companion.AUTHORIZATION_HEADER_NAME
import gq.kirmanak.mealient.datasource.models.*
import retrofit2.Response
import retrofit2.http.*

interface MealieService {

    @FormUrlEncoded
    @POST
    suspend fun getToken(
        @Url url: String,
        @Field("username") username: String,
        @Field("password") password: String,
    ): Response<GetTokenResponse>

    @POST
    suspend fun addRecipe(
        @Url url: String,
        @Header(AUTHORIZATION_HEADER_NAME) token: String?,
        @Body addRecipeRequest: AddRecipeRequest,
    ): String

    @GET
    suspend fun getVersion(
        @Url url: String,
    ): VersionResponse

    @GET
    suspend fun getRecipeSummary(
        @Url url: String,
        @Header(AUTHORIZATION_HEADER_NAME) token: String?,
        @Query("start") start: Int,
        @Query("limit") limit: Int,
    ): List<GetRecipeSummaryResponse>

    @GET
    suspend fun getRecipe(
        @Url url: String,
        @Header(AUTHORIZATION_HEADER_NAME) token: String?,
    ): GetRecipeResponse
}