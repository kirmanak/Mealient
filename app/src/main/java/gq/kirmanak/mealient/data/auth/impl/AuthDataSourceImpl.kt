package gq.kirmanak.mealient.data.auth.impl

import gq.kirmanak.mealient.data.auth.AuthDataSource
import gq.kirmanak.mealient.data.auth.AuthService
import gq.kirmanak.mealient.data.impl.RetrofitBuilder
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
    ): String {
        Timber.v("authenticate() called with: username = $username, password = $password, baseUrl = $baseUrl")
        val authService = retrofitBuilder.buildRetrofit(baseUrl).create<AuthService>()
        val response = authService.getToken(username, password)
        Timber.d("authenticate() response is $response")
        return response.accessToken
    }
}