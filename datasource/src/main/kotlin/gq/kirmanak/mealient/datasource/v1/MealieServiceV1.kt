package gq.kirmanak.mealient.datasource.v1

import gq.kirmanak.mealient.datasource.v1.models.*
import retrofit2.http.*

interface MealieServiceV1 {

    @FormUrlEncoded
    @POST
    suspend fun getToken(
        @Url url: String,
        @Field("username") username: String,
        @Field("password") password: String,
    ): GetTokenResponseV1

    @POST
    suspend fun createRecipe(
        @Url url: String,
        @Body addRecipeRequest: CreateRecipeRequestV1,
    ): String

    @PATCH
    suspend fun updateRecipe(
        @Url url: String,
        @Body addRecipeRequest: UpdateRecipeRequestV1,
    ): GetRecipeResponseV1

    @GET
    suspend fun getVersion(
        @Url url: String,
    ): VersionResponseV1

    @GET
    suspend fun getRecipeSummary(
        @Url url: String,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int,
    ): GetRecipesResponseV1

    @GET
    suspend fun getRecipe(
        @Url url: String,
    ): GetRecipeResponseV1

    @POST
    suspend fun createRecipeFromURL(
        @Url url: String,
        @Body request: ParseRecipeURLRequestV1,
    ): String
}