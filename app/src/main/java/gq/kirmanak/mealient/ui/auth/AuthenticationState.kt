package gq.kirmanak.mealient.ui.auth

import timber.log.Timber

enum class AuthenticationState {
    AUTHORIZED,
    AUTH_REQUESTED,
    UNAUTHORIZED;

    companion object {

        fun determineState(
            isLoginRequested: Boolean,
            isAuthorized: Boolean,
        ): AuthenticationState {
            Timber.v("determineState() called with: isLoginRequested = $isLoginRequested, isAuthorized = $isAuthorized")
            val result = when {
                isAuthorized -> AUTHORIZED
                isLoginRequested -> AUTH_REQUESTED
                else -> UNAUTHORIZED
            }
            Timber.v("determineState() returned: $result")
            return result
        }
    }
}
