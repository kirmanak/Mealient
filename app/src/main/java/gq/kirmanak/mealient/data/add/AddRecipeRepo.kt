package gq.kirmanak.mealient.data.add

import gq.kirmanak.mealient.datasource.models.AddRecipeRequest
import kotlinx.coroutines.flow.Flow

interface AddRecipeRepo {

    val addRecipeRequestFlow: Flow<AddRecipeRequest>

    suspend fun preserve(recipe: AddRecipeRequest)

    suspend fun clear()

    suspend fun saveRecipe(): String
}