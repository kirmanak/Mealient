package gq.kirmanak.mealient.ui.activity

data class MainActivityUiState(
    val loginButtonVisible: Boolean = false,
    val titleVisible: Boolean = true,
    val isAuthorized: Boolean = false,
    val navigationVisible: Boolean = false,
) {
    val canShowLogin: Boolean
        get() = !isAuthorized && loginButtonVisible

    val canShowLogout: Boolean
        get() = isAuthorized && loginButtonVisible
}
