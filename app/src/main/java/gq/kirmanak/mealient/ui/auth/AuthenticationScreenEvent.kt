package gq.kirmanak.mealient.ui.auth

internal sealed interface AuthenticationScreenEvent {

    data class OnEmailInput(val input: String) : AuthenticationScreenEvent

    data class OnPasswordInput(val input: String) : AuthenticationScreenEvent

    data object OnLoginClick : AuthenticationScreenEvent

    data object TogglePasswordVisibility : AuthenticationScreenEvent
}