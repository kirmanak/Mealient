package gq.kirmanak.mealient.data.add

import gq.kirmanak.mealient.datasource.v0.models.AddRecipeRequestV0
import kotlinx.coroutines.flow.Flow

interface AddRecipeRepo {

    val addRecipeRequestFlow: Flow<AddRecipeRequestV0>

    suspend fun preserve(recipe: AddRecipeRequestV0)

    suspend fun clear()

    suspend fun saveRecipe(): String
}