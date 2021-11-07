package gq.kirmanak.mealie.data.recipes.network

import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeService {
    @GET("/api/recipes/summary")
    suspend fun getRecipeSummary(
        @Query("start") start: Int,
        @Query("end") end: Int
    ): List<GetRecipeSummaryResponse>
}