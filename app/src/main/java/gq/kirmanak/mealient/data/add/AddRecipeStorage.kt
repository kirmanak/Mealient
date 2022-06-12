package gq.kirmanak.mealient.data.add

import gq.kirmanak.mealient.data.add.models.AddRecipeRequest
import kotlinx.coroutines.flow.Flow

interface AddRecipeStorage {

    val updates: Flow<AddRecipeRequest>

    suspend fun save(addRecipeRequest: AddRecipeRequest)

    suspend fun clear()
}