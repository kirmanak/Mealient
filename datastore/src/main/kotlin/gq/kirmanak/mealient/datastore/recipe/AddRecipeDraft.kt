package gq.kirmanak.mealient.datastore.recipe

data class AddRecipeDraft(
    val recipeName: String,
    val recipeDescription: String,
    val recipeYield: String,
    val recipeInstructions: List<String>,
    val recipeIngredients: List<String>,
    val isRecipePublic: Boolean,
    val areCommentsDisabled: Boolean,
)
