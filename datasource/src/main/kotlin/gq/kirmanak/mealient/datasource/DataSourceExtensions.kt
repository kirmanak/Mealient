package gq.kirmanak.mealient.datasource

import kotlinx.coroutines.CancellationException
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import okhttp3.ResponseBody

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

@OptIn(ExperimentalSerializationApi::class)
inline fun <reified R> ResponseBody.decode(json: Json): R = json.decodeFromStream(byteStream())

inline fun <reified T> Throwable.findCauseAsInstanceOf(): T? {
    var cause: Throwable? = this
    var previousCause: Throwable? = null
    while (cause != null && cause != previousCause && cause !is T) {
        previousCause = cause
        cause = cause.cause
    }
    return cause as? T
}