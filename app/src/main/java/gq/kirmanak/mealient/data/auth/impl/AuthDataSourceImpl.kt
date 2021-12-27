package gq.kirmanak.mealient.data.auth.impl

import gq.kirmanak.mealient.data.auth.AuthDataSource
import gq.kirmanak.mealient.data.auth.impl.AuthenticationError.*
import gq.kirmanak.mealient.data.impl.ErrorDetail
import gq.kirmanak.mealient.data.impl.RetrofitBuilder
import gq.kirmanak.mealient.data.impl.util.decodeErrorBodyOrNull
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import retrofit2.create
import timber.log.Timber
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
            val response = authService.getToken(username, password)
            Timber.d("authenticate() response is $response")
            if (response.isSuccessful) {
                checkNotNull(response.body()).accessToken
            } else {
                val cause = HttpException(response)
                val errorDetail: ErrorDetail? = response.decodeErrorBodyOrNull(json)
                throw when (errorDetail?.detail) {
                    "Unauthorized" -> Unauthorized(cause)
                    else -> NotMealie(cause)
                }
            }
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
}