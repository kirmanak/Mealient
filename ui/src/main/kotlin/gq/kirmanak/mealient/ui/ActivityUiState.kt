package gq.kirmanak.mealient.ui

data class ActivityUiState(
    val isAuthorized: Boolean = false,
    val navigationVisible: Boolean = false,
    val searchVisible: Boolean = false,
    val checkedMenuItem: CheckableMenuItem? = null,
    val v1MenuItemsVisible: Boolean = false,
) {
    val canShowLogin: Boolean get() = !isAuthorized

    val canShowLogout: Boolean get() = isAuthorized
}

enum class CheckableMenuItem {
    ShoppingLists,
    RecipesList,
    AddRecipe,
    ChangeUrl,
    Login
}
