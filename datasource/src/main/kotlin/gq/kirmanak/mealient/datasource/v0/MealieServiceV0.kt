package gq.kirmanak.mealient.datasource.v0

import gq.kirmanak.mealient.datasource.v0.models.*
import retrofit2.http.*

interface MealieServiceV0 {

    @FormUrlEncoded
    @POST("/api/auth/token")
    suspend fun getToken(
        @Field("username") username: String,
        @Field("password") password: String,
    ): GetTokenResponseV0

    @POST("/api/recipes/create")
    suspend fun addRecipe(
        @Body addRecipeRequestV0: AddRecipeRequestV0,
    ): String

    @GET("/api/debug/version")
    suspend fun getVersion(): VersionResponseV0

    @GET("/api/recipes/summary")
    suspend fun getRecipeSummary(
        @Query("start") start: Int,
        @Query("limit") limit: Int,
    ): List<GetRecipeSummaryResponseV0>

    @GET("/api/recipes/{slug}")
    suspend fun getRecipe(
        @Path("slug") slug: String,
    ): GetRecipeResponseV0

    @POST("/api/recipes/create-url")
    suspend fun createRecipeFromURL(
        @Body request: ParseRecipeURLRequestV0,
    ): String

    @POST("/api/users/api-tokens")
    suspend fun createApiToken(
        @Body request: CreateApiTokenRequestV0,
    ): String

    @GET("/api/users/self")
    suspend fun getUserSelfInfo(): GetUserInfoResponseV0
}