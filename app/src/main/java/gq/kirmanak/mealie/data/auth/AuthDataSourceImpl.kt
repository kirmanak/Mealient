package gq.kirmanak.mealie.data.auth

import gq.kirmanak.mealie.data.RetrofitBuilder
import kotlinx.serialization.ExperimentalSerializationApi
import retrofit2.create
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
        val authService = retrofitBuilder.buildRetrofit(baseUrl).create<AuthService>()
        val response = try {
            authService.getToken(username, password)
        } catch (e: Exception) {
            return Result.failure(e)
        }
        return Result.success(response.accessToken)
    }
}