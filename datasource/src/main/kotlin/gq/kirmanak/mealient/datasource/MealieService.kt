package gq.kirmanak.mealient.datasource

import gq.kirmanak.mealient.datasource.models.CreateApiTokenRequest
import gq.kirmanak.mealient.datasource.models.CreateApiTokenResponse
import gq.kirmanak.mealient.datasource.models.CreateRecipeRequest
import gq.kirmanak.mealient.datasource.models.CreateShoppingListItemRequest
import gq.kirmanak.mealient.datasource.models.GetFoodsResponse
import gq.kirmanak.mealient.datasource.models.GetRecipeResponse
import gq.kirmanak.mealient.datasource.models.GetRecipesResponse
import gq.kirmanak.mealient.datasource.models.GetShoppingListResponse
import gq.kirmanak.mealient.datasource.models.GetShoppingListsResponse
import gq.kirmanak.mealient.datasource.models.GetTokenResponse
import gq.kirmanak.mealient.datasource.models.GetUnitsResponse
import gq.kirmanak.mealient.datasource.models.GetUserInfoResponse
import gq.kirmanak.mealient.datasource.models.ParseRecipeURLRequest
import gq.kirmanak.mealient.datasource.models.UpdateRecipeRequest
import gq.kirmanak.mealient.datasource.models.VersionResponse
import kotlinx.serialization.json.JsonElement
import retrofit2.http.*

interface MealieService {

    @FormUrlEncoded
    @POST("/api/auth/token")
    suspend fun getToken(
        @Field("username") username: String,
        @Field("password") password: String,
    ): GetTokenResponse

    @POST("/api/recipes")
    suspend fun createRecipe(
        @Body addRecipeRequest: CreateRecipeRequest,
    ): String

    @PATCH("/api/recipes/{slug}")
    suspend fun updateRecipe(
        @Body addRecipeRequest: UpdateRecipeRequest,
        @Path("slug") slug: String,
    ): GetRecipeResponse

    @GET("/api/app/about")
    suspend fun getVersion(): VersionResponse

    @GET("/api/recipes")
    suspend fun getRecipeSummary(
        @Query("page") page: Int,
        @Query("perPage") perPage: Int,
    ): GetRecipesResponse

    @GET("/api/recipes/{slug}")
    suspend fun getRecipe(
        @Path("slug") slug: String,
    ): GetRecipeResponse

    @POST("/api/recipes/create-url")
    suspend fun createRecipeFromURL(
        @Body request: ParseRecipeURLRequest,
    ): String

    @POST("/api/users/api-tokens")
    suspend fun createApiToken(
        @Body request: CreateApiTokenRequest,
    ): CreateApiTokenResponse

    @GET("/api/users/self")
    suspend fun getUserSelfInfo(): GetUserInfoResponse

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
    ): GetShoppingListsResponse

    @GET("/api/groups/shopping/lists/{id}")
    suspend fun getShoppingList(
        @Path("id") id: String,
    ): GetShoppingListResponse

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
    ): GetFoodsResponse

    @GET("/api/units")
    suspend fun getUnits(
        @Query("perPage") perPage: Int,
    ): GetUnitsResponse

    @POST("/api/groups/shopping/items")
    suspend fun createShoppingListItem(
        @Body request: CreateShoppingListItemRequest,
    )
}