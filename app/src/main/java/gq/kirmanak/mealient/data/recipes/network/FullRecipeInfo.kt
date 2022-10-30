package gq.kirmanak.mealient.data.recipes.network

data class FullRecipeInfo(
    val remoteId: String,
    val name: String,
    val recipeYield: String,
    val recipeIngredients: List<RecipeIngredientInfo>,
    val recipeInstructions: List<RecipeInstructionInfo>,
)

data class RecipeIngredientInfo(
    val note: String,
)

data class RecipeInstructionInfo(
    val text: String,
)
