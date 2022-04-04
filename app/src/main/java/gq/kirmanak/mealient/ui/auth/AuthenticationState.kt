package gq.kirmanak.mealient.ui.auth

import timber.log.Timber

enum class AuthenticationState {
    AUTHORIZED,
    AUTH_REQUESTED,
    UNAUTHORIZED,
    UNKNOWN;

    companion object {

        fun determineState(
            isLoginRequested: Boolean,
            showLoginButton: Boolean,
            isAuthorized: Boolean,
        ): AuthenticationState {
            Timber.v("determineState() called with: isLoginRequested = $isLoginRequested, showLoginButton = $showLoginButton, isAuthorized = $isAuthorized")
            val result = when {
                !showLoginButton -> UNKNOWN
                isAuthorized -> AUTHORIZED
                isLoginRequested -> AUTH_REQUESTED
                else -> UNAUTHORIZED
            }
            Timber.v("determineState() returned: $result")
            return result
        }
    }
}
