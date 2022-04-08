package gq.kirmanak.mealient.data.network

import gq.kirmanak.mealient.data.auth.AuthRepo
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticationInterceptor @Inject constructor(
    private val authRepo: AuthRepo,
) : Interceptor {

    private val authHeader: String?
        get() = runBlocking { authRepo.getAuthHeader() }

    override fun intercept(chain: Interceptor.Chain): Response {
        val currentHeader = authHeader ?: return chain.proceed(chain.request())
        val response = proceedWithAuthHeader(chain, currentHeader)
        return if (listOf(401, 403).contains(response.code)) {
            runBlocking { authRepo.invalidateAuthHeader() }
            // Try again with new auth header (if any) or return previous response
            authHeader?.let { proceedWithAuthHeader(chain, it) } ?: response
        } else {
            response
        }
    }

    private fun proceedWithAuthHeader(
        chain: Interceptor.Chain,
        authHeader: String,
    ) = chain.proceed(
        chain.request()
            .newBuilder()
            .header(HEADER_NAME, authHeader)
            .build()
    )

    companion object {
        private const val HEADER_NAME = "Authorization"
    }
}