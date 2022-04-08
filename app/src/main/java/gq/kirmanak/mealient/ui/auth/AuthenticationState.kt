package gq.kirmanak.mealient.ui.auth

import timber.log.Timber

enum class AuthenticationState {
    AUTHORIZED,
    UNAUTHORIZED,
    UNKNOWN;

    companion object {

        fun determineState(
            showLoginButton: Boolean,
            isAuthorized: Boolean,
        ): AuthenticationState {
            Timber.v("determineState() called with: showLoginButton = $showLoginButton, isAuthorized = $isAuthorized")
            val result = when {
                !showLoginButton -> UNKNOWN
                isAuthorized -> AUTHORIZED
                else -> UNAUTHORIZED
            }
            Timber.v("determineState() returned: $result")
            return result
        }
    }
}
