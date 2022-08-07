package gq.kirmanak.mealient.data.add

import gq.kirmanak.mealient.datasource.models.AddRecipeRequest

interface AddRecipeDataSource {
    suspend fun addRecipe(recipe: AddRecipeRequest): String
}