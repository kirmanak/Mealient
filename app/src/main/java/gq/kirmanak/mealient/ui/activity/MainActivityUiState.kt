package gq.kirmanak.mealient.ui.activity

data class MainActivityUiState(
    val isAuthorized: Boolean = false,
    val navigationVisible: Boolean = false,
    val searchVisible: Boolean = false,
) {
    val canShowLogin: Boolean get() = !isAuthorized

    val canShowLogout: Boolean get() = isAuthorized
}
