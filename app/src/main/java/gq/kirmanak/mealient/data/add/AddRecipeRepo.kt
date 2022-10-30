package gq.kirmanak.mealient.data.add

import kotlinx.coroutines.flow.Flow

interface AddRecipeRepo {

    val addRecipeRequestFlow: Flow<AddRecipeInfo>

    suspend fun preserve(recipe: AddRecipeInfo)

    suspend fun clear()

    suspend fun saveRecipe(): String
}