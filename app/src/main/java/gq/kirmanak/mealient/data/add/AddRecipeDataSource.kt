package gq.kirmanak.mealient.data.add

import gq.kirmanak.mealient.datasource.v0.models.AddRecipeRequestV0

interface AddRecipeDataSource {
    suspend fun addRecipe(recipe: AddRecipeRequestV0): String
}