package gq.kirmanak.mealient.datasource

import kotlinx.coroutines.CancellationException

/**
 * Like [runCatching] but rethrows [CancellationException] to support
 * cancellation of coroutines.
 */
inline fun <T> runCatchingExceptCancel(block: () -> T): Result<T> = try {
    Result.success(block())
} catch (e: CancellationException) {
    throw e
} catch (e: Throwable) {
    Result.failure(e)
}

inline fun <reified T> Throwable.findCauseAsInstanceOf(): T? {
    var cause: Throwable? = this
    var previousCause: Throwable? = null
    while (cause != null && cause != previousCause && cause !is T) {
        previousCause = cause
        cause = cause.cause
    }
    return cause as? T
}