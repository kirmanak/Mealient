package gq.kirmanak.mealient.data.add

import gq.kirmanak.mealient.datasource.models.AddRecipeInfo

interface AddRecipeDataSource {

    suspend fun addRecipe(recipe: AddRecipeInfo): String
}