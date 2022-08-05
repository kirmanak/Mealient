package gq.kirmanak.mealient.extensions

import gq.kirmanak.mealient.data.network.NetworkError
import gq.kirmanak.mealient.logging.Logger
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import retrofit2.HttpException
import retrofit2.Response

@OptIn(ExperimentalSerializationApi::class)
inline fun <T, reified R> Json.decodeErrorBody(response: Response<T>): R =
    checkNotNull(response.errorBody()) { "Can't decode absent error body" }
        .byteStream()
        .let(::decodeFromStream)


fun Throwable.mapToNetworkError(): NetworkError = when (this) {
    is HttpException, is SerializationException -> NetworkError.NotMealie(this)
    else -> NetworkError.NoServerConnection(this)
}

inline fun <T> Logger.logAndMapErrors(
    block: () -> T,
    noinline logProvider: () -> String
): T = runCatchingExceptCancel(block).getOrElse {
    e(it, messageSupplier = logProvider)
    throw it.mapToNetworkError()
}