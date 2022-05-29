package gq.kirmanak.mealient.extensions

import gq.kirmanak.mealient.data.network.NetworkError
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.io.InputStream

inline fun <T, reified R> Response<T>.decodeErrorBodyOrNull(json: Json): R? =
    errorBody()?.byteStream()?.let { json.decodeFromStreamOrNull<R>(it) }

@OptIn(ExperimentalSerializationApi::class)
inline fun <reified T> Json.decodeFromStreamOrNull(stream: InputStream): T? =
    runCatching { decodeFromStream<T>(stream) }
        .onFailure { Timber.e(it, "decodeFromStreamOrNull: can't decode") }
        .getOrNull()

fun Throwable.mapToNetworkError(): NetworkError = when (this) {
    is HttpException, is SerializationException -> NetworkError.NotMealie(this)
    else -> NetworkError.NoServerConnection(this)
}
