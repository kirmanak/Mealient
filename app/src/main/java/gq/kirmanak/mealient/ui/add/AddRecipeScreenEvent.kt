package gq.kirmanak.mealient.ui.add

internal sealed interface AddRecipeScreenEvent {

    data class RecipeNameInput(
        val input: String,
    ) : AddRecipeScreenEvent

    data class RecipeDescriptionInput(
        val input: String,
    ) : AddRecipeScreenEvent

    data class RecipeYieldInput(
        val input: String,
    ) : AddRecipeScreenEvent

    data object PublicRecipeToggle : AddRecipeScreenEvent

    data object DisableCommentsToggle : AddRecipeScreenEvent

    data object AddIngredientClick : AddRecipeScreenEvent

    data object AddInstructionClick : AddRecipeScreenEvent

    data object SaveRecipeClick : AddRecipeScreenEvent

    data class IngredientInput(
        val ingredientIndex: Int,
        val input: String,
    ) : AddRecipeScreenEvent

    data class InstructionInput(
        val instructionIndex: Int,
        val input: String,
    ) : AddRecipeScreenEvent

    data object ClearInputClick : AddRecipeScreenEvent

    data object SnackbarShown : AddRecipeScreenEvent

    data class RemoveIngredientClick(
        val ingredientIndex: Int,
    ) : AddRecipeScreenEvent

    data class RemoveInstructionClick(
        val instructionIndex: Int,
    ) : AddRecipeScreenEvent
}