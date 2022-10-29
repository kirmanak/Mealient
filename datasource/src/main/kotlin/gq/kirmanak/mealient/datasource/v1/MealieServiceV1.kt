package gq.kirmanak.mealient.datasource.v1

import gq.kirmanak.mealient.datasource.DataSourceModule.Companion.AUTHORIZATION_HEADER_NAME
import gq.kirmanak.mealient.datasource.v0.models.AddRecipeRequestV0
import gq.kirmanak.mealient.datasource.v1.models.GetRecipeResponseV1
import gq.kirmanak.mealient.datasource.v1.models.GetRecipesResponseV1
import gq.kirmanak.mealient.datasource.v1.models.GetTokenResponseV1
import gq.kirmanak.mealient.datasource.v1.models.VersionResponseV1
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
    suspend fun addRecipe(
        @Url url: String,
        @Header(AUTHORIZATION_HEADER_NAME) token: String?,
        @Body addRecipeRequestV0: AddRecipeRequestV0,
    ): String

    @GET
    suspend fun getVersion(
        @Url url: String,
    ): VersionResponseV1

    @GET
    suspend fun getRecipeSummary(
        @Url url: String,
        @Header(AUTHORIZATION_HEADER_NAME) token: String?,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int,
    ): GetRecipesResponseV1

    @GET
    suspend fun getRecipe(
        @Url url: String,
        @Header(AUTHORIZATION_HEADER_NAME) token: String?,
    ): GetRecipeResponseV1
}