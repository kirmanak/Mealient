package gq.kirmanak.mealient.extensions

import gq.kirmanak.mealient.data.network.NetworkError
import gq.kirmanak.mealient.logging.Logger
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import retrofit2.HttpException
import retrofit2.Response
import java.io.InputStream

inline fun <T, reified R> Response<T>.decodeErrorBodyOrNull(json: Json, logger: Logger): R? =
    errorBody()?.byteStream()?.let { json.decodeFromStreamOrNull<R>(it, logger) }

@OptIn(ExperimentalSerializationApi::class)
inline fun <reified T> Json.decodeFromStreamOrNull(stream: InputStream, logger: Logger): T? =
    runCatching { decodeFromStream<T>(stream) }
        .onFailure { logger.e(it) { "decodeFromStreamOrNull: can't decode" } }
        .getOrNull()

fun Throwable.mapToNetworkError(): NetworkError = when (this) {
    is HttpException, is SerializationException -> NetworkError.NotMealie(this)
    else -> NetworkError.NoServerConnection(this)
}

inline fun <T> logAndMapErrors(
    logger: Logger,
    block: () -> T,
    noinline logProvider: () -> String
): T =
    runCatchingExceptCancel(block).getOrElse {
        logger.e(it, messageSupplier = logProvider)
        throw it.mapToNetworkError()
    }