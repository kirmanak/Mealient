package gq.kirmanak.mealient.data.network

sealed class NetworkError(cause: Throwable) : RuntimeException(cause) {
    class Unauthorized(cause: Throwable) : NetworkError(cause)
    class NoServerConnection(cause: Throwable) : NetworkError(cause)
    class NotMealie(cause: Throwable) : NetworkError(cause)
    class MalformedUrl(cause: Throwable) : NetworkError(cause)
}
