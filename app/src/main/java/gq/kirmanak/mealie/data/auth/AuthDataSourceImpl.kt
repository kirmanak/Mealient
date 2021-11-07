package gq.kirmanak.mealie.data.auth

import gq.kirmanak.mealie.data.RetrofitBuilder
import kotlinx.serialization.ExperimentalSerializationApi
import retrofit2.create
import timber.log.Timber
import javax.inject.Inject

@ExperimentalSerializationApi
class AuthDataSourceImpl @Inject constructor(
    private val retrofitBuilder: RetrofitBuilder
) : AuthDataSource {
    override suspend fun authenticate(
        username: String,
        password: String,
        baseUrl: String
    ): Result<String> {
        Timber.v("authenticate() called with: username = $username, password = $password, baseUrl = $baseUrl")
        val authService = retrofitBuilder.buildRetrofit(baseUrl).create<AuthService>()
        val response = try {
            authService.getToken(username, password)
        } catch (e: Exception) {
            Timber.e("Authenticate() exception", e)
            return Result.failure(e)
        }
        Timber.d("authenticate() response is $response")
        return Result.success(response.accessToken)
    }
}