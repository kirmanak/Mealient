package gq.kirmanak.mealient.data.auth.impl

import gq.kirmanak.mealient.data.auth.AuthDataSource
import gq.kirmanak.mealient.data.network.ErrorDetail
import gq.kirmanak.mealient.data.network.NetworkError.NotMealie
import gq.kirmanak.mealient.data.network.NetworkError.Unauthorized
import gq.kirmanak.mealient.data.network.ServiceFactory
import gq.kirmanak.mealient.extensions.decodeErrorBodyOrNull
import gq.kirmanak.mealient.extensions.logAndMapErrors
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthDataSourceImpl @Inject constructor(
    private val authServiceFactory: ServiceFactory<AuthService>,
    private val json: Json,
) : AuthDataSource {

    override suspend fun authenticate(username: String, password: String): String {
        Timber.v("authenticate() called with: username = $username, password = $password")
        val authService = authServiceFactory.provideService()
        val response = sendRequest(authService, username, password)
        val accessToken = parseToken(response)
        Timber.v("authenticate() returned: $accessToken")
        return accessToken
    }

    private suspend fun sendRequest(
        authService: AuthService,
        username: String,
        password: String
    ): Response<GetTokenResponse> = logAndMapErrors(
        block = { authService.getToken(username = username, password = password) },
        logProvider = { "sendRequest: can't get token" },
    )

    private fun parseToken(
        response: Response<GetTokenResponse>
    ): String = if (response.isSuccessful) {
        response.body()?.accessToken ?: throw NotMealie(NullPointerException("Body is null"))
    } else {
        val cause = HttpException(response)
        val errorDetail: ErrorDetail? = response.decodeErrorBodyOrNull(json)
        throw when (errorDetail?.detail) {
            "Unauthorized" -> Unauthorized(cause)
            else -> NotMealie(cause)
        }
    }
}