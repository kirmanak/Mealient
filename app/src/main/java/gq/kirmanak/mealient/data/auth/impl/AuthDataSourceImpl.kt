package gq.kirmanak.mealient.data.auth.impl

import gq.kirmanak.mealient.data.auth.AuthDataSource
import gq.kirmanak.mealient.data.auth.AuthService
import gq.kirmanak.mealient.data.auth.impl.AuthenticationError.*
import gq.kirmanak.mealient.data.impl.ErrorDetail
import gq.kirmanak.mealient.data.impl.RetrofitBuilder
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.create
import timber.log.Timber
import java.io.InputStream
import javax.inject.Inject

@ExperimentalSerializationApi
class AuthDataSourceImpl @Inject constructor(
    private val retrofitBuilder: RetrofitBuilder,
    private val json: Json,
) : AuthDataSource {
    override suspend fun authenticate(
        username: String,
        password: String,
        baseUrl: String
    ): String {
        Timber.v("authenticate() called with: username = $username, password = $password, baseUrl = $baseUrl")
        val authService = retrofitBuilder.buildRetrofit(baseUrl).create<AuthService>()

        val accessToken = runCatching {
            authService.getToken(username, password)
        }.mapCatching {
            Timber.d("authenticate() response is $it")
            if (!it.isSuccessful) {
                val cause = HttpException(it)
                throw when (it.decodeErrorBodyOrNull<GetTokenResponse, ErrorDetail>()?.detail) {
                    "Unauthorized" -> Unauthorized(cause)
                    else -> NotMealie(cause)
                }
            }
            checkNotNull(it.body()).accessToken // Can't be null here, would throw SerializationException
        }.onFailure {
            Timber.e(it, "authenticate: getToken failed")
            throw when (it) {
                is CancellationException, is AuthenticationError -> it
                is SerializationException, is IllegalStateException -> NotMealie(it)
                else -> NoServerConnection(it)
            }
        }.getOrThrow()

        Timber.v("authenticate() returned: $accessToken")
        return accessToken
    }

    private inline fun <reified T, reified R> Response<T>.decodeErrorBodyOrNull(): R? =
        errorBody()?.byteStream()?.let { json.decodeFromStreamOrNull<R>(it) }

    private inline fun <reified T> Json.decodeFromStreamOrNull(stream: InputStream): T? =
        runCatching { decodeFromStream<T>(stream) }
            .onFailure { Timber.e(it, "decodeFromStreamOrNull: can't decode") }
            .getOrNull()
}