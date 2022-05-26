package gq.kirmanak.mealient.data.add.impl

import gq.kirmanak.mealient.data.add.models.AddRecipeRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AddRecipeService {

    @POST("/api/recipes/create")
    suspend fun addRecipe(@Body addRecipeRequest: AddRecipeRequest): String

}
