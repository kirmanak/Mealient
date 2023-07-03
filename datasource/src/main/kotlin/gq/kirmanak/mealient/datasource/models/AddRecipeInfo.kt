package gq.kirmanak.mealient.datasource.models

data class AddRecipeInfo(
    val name: String,
    val description: String,
    val recipeYield: String,
    val recipeIngredient: List<AddRecipeIngredientInfo>,
    val recipeInstructions: List<AddRecipeInstructionInfo>,
    val settings: AddRecipeSettingsInfo,
)

data class AddRecipeSettingsInfo(
    val disableComments: Boolean,
    val public: Boolean,
)

data class AddRecipeIngredientInfo(
    val note: String,
)

data class AddRecipeInstructionInfo(
    val text: String,
)
