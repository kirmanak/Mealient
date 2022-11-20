package gq.kirmanak.mealient.ui.activity

import androidx.annotation.IdRes

data class MainActivityUiState(
    val isAuthorized: Boolean = false,
    val navigationVisible: Boolean = false,
    val searchVisible: Boolean = false,
    @IdRes val checkedMenuItemId: Int? = null,
) {
    val canShowLogin: Boolean get() = !isAuthorized

    val canShowLogout: Boolean get() = isAuthorized
}
