package gq.kirmanak.mealient.data.add

import gq.kirmanak.mealient.data.add.models.AddRecipeRequest

interface AddRecipeDataSource {

    suspend fun addRecipe(recipe: AddRecipeRequest): String
}