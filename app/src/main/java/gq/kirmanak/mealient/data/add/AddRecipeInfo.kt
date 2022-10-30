package gq.kirmanak.mealient.data.add

data class AddRecipeInfo(
    val name: String = "",
    val description: String = "",
    val image: String = "",
    val recipeYield: String = "",
    val recipeIngredient: List<AddRecipeIngredientInfo> = emptyList(),
    val recipeInstructions: List<AddRecipeInstructionInfo> = emptyList(),
    val slug: String = "",
    val filePath: String = "",
    val tags: List<String> = emptyList(),
    val categories: List<String> = emptyList(),
    val notes: List<AddRecipeNoteInfo> = emptyList(),
    val extras: Map<String, String> = emptyMap(),
    val assets: List<String> = emptyList(),
    val settings: AddRecipeSettingsInfo = AddRecipeSettingsInfo(),
)

data class AddRecipeSettingsInfo(
    val disableAmount: Boolean = true,
    val disableComments: Boolean = false,
    val landscapeView: Boolean = true,
    val public: Boolean = true,
    val showAssets: Boolean = true,
    val showNutrition: Boolean = true,
)

data class AddRecipeNoteInfo(
    val title: String = "",
    val text: String = "",
)

data class AddRecipeIngredientInfo(
    val disableAmount: Boolean = true,
    val food: String? = null,
    val note: String = "",
    val quantity: Int = 1,
    val title: String? = null,
    val unit: String? = null,
)

data class AddRecipeInstructionInfo(
    val title: String = "",
    val text: String = "",
)
