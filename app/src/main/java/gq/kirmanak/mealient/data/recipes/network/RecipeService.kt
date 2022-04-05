package gq.kirmanak.mealient.data.recipes.network

import gq.kirmanak.mealient.data.recipes.network.response.GetRecipeResponse
import gq.kirmanak.mealient.data.recipes.network.response.GetRecipeSummaryResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeService {
    @GET("/api/recipes/summary")
    suspend fun getRecipeSummary(
        @Query("start") start: Int,
        @Query("limit") limit: Int,
    ): List<GetRecipeSummaryResponse>

    @GET("/api/recipes/{recipe_slug}")
    suspend fun getRecipe(
        @Path("recipe_slug") recipeSlug: String,
    ): GetRecipeResponse
}