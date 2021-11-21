package gq.kirmanak.mealient.data.auth.impl

sealed class AuthenticationError(cause: Throwable) : RuntimeException(cause) {
    class Unauthorized(cause: Throwable) : AuthenticationError(cause)
    class NoServerConnection(cause: Throwable) : AuthenticationError(cause)
    class NotMealie(cause: Throwable) : AuthenticationError(cause)
}
