package gq.kirmanak.mealient.ui.add

internal sealed interface AddRecipeSnackbarMessage {

    data object Success : AddRecipeSnackbarMessage

    data object Error : AddRecipeSnackbarMessage
}