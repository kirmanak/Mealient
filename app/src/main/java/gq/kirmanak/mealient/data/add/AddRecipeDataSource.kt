package gq.kirmanak.mealient.data.add

interface AddRecipeDataSource {

    suspend fun addRecipe(recipe: AddRecipeInfo): String
}