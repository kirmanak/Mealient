package gq.kirmanak.mealient.ui.add

internal data class AddRecipeScreenState(
    val snackbarMessage: AddRecipeSnackbarMessage? = null,
    val isLoading: Boolean = false,
    val recipeNameInput: String = "",
    val recipeDescriptionInput: String = "",
    val recipeYieldInput: String = "",
    val isPublicRecipe: Boolean = false,
    val disableComments: Boolean = false,
    val saveButtonEnabled: Boolean = false,
    val clearButtonEnabled: Boolean = true,
    val ingredients: List<String> = emptyList(),
    val instructions: List<String> = emptyList(),
)