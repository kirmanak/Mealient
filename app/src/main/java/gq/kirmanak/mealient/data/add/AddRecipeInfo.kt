package gq.kirmanak.mealient.data.add

data class AddRecipeInfo(
    val name: String = "",
    val description: String = "",
    val recipeYield: String = "",
    val recipeIngredient: List<AddRecipeIngredientInfo> = emptyList(),
    val recipeInstructions: List<AddRecipeInstructionInfo> = emptyList(),
    val settings: AddRecipeSettingsInfo = AddRecipeSettingsInfo(),
)

data class AddRecipeSettingsInfo(
    val disableComments: Boolean = false,
    val public: Boolean = true,
)

data class AddRecipeIngredientInfo(
    val note: String = "",
)

data class AddRecipeInstructionInfo(
    val text: String = "",
)
