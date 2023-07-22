package gq.kirmanak.mealient.datasource.v1

import gq.kirmanak.mealient.datasource.v1.models.*
import kotlinx.serialization.json.JsonElement
import retrofit2.http.*

interface MealieServiceV1 {

    @FormUrlEncoded
    @POST("/api/auth/token")
    suspend fun getToken(
        @Field("username") username: String,
        @Field("password") password: String,
    ): GetTokenResponseV1

    @POST("/api/recipes")
    suspend fun createRecipe(
        @Body addRecipeRequest: CreateRecipeRequestV1,
    ): String

    @PATCH("/api/recipes/{slug}")
    suspend fun updateRecipe(
        @Body addRecipeRequest: UpdateRecipeRequestV1,
        @Path("slug") slug: String,
    ): GetRecipeResponseV1

    @GET("/api/app/about")
    suspend fun getVersion(): VersionResponseV1

    @GET("/api/recipes")
    suspend fun getRecipeSummary(
        @Query("page") page: Int,
        @Query("perPage") perPage: Int,
    ): GetRecipesResponseV1

    @GET("/api/recipes/{slug}")
    suspend fun getRecipe(
        @Path("slug") slug: String,
    ): GetRecipeResponseV1

    @POST("/api/recipes/create-url")
    suspend fun createRecipeFromURL(
        @Body request: ParseRecipeURLRequestV1,
    ): String

    @POST("/api/users/api-tokens")
    suspend fun createApiToken(
        @Body request: CreateApiTokenRequestV1,
    ): CreateApiTokenResponseV1

    @GET("/api/users/self")
    suspend fun getUserSelfInfo(): GetUserInfoResponseV1

    @DELETE("/api/users/{userId}/favorites/{recipeSlug}")
    suspend fun removeFavoriteRecipe(
        @Path("userId") userId: String,
        @Path("recipeSlug") recipeSlug: String
    )

    @POST("/api/users/{userId}/favorites/{recipeSlug}")
    suspend fun addFavoriteRecipe(
        @Path("userId") userId: String,
        @Path("recipeSlug") recipeSlug: String
    )

    @DELETE("/api/recipes/{slug}")
    suspend fun deleteRecipe(
        @Path("slug") slug: String
    )

    @GET("/api/groups/shopping/lists")
    suspend fun getShoppingLists(
        @Query("page") page: Int,
        @Query("perPage") perPage: Int,
    ): GetShoppingListsResponseV1

    @GET("/api/groups/shopping/lists/{id}")
    suspend fun getShoppingList(
        @Path("id") id: String,
    ): GetShoppingListResponseV1

    @GET("/api/groups/shopping/items/{id}")
    suspend fun getShoppingListItem(
        @Path("id") id: String,
    ): JsonElement

    @PUT("/api/groups/shopping/items/{id}")
    suspend fun updateShoppingListItem(
        @Path("id") id: String,
        @Body request: JsonElement,
    )

    @DELETE("/api/groups/shopping/items/{id}")
    suspend fun deleteShoppingListItem(
        @Path("id") id: String,
    )

    @GET("/api/foods")
    suspend fun getFoods(
        @Query("perPage") perPage: Int,
    ): GetFoodsResponseV1

    @GET("/api/units")
    suspend fun getUnits(
        @Query("perPage") perPage: Int,
    ): GetUnitsResponseV1
}