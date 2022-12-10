package gq.kirmanak.mealient.datasource.impl

import androidx.annotation.VisibleForTesting
import gq.kirmanak.mealient.datasource.AuthenticationProvider
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val authenticationProvider: Provider<AuthenticationProvider>,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = getAuthHeader()
        return if (token == null) {
            proceedWithAuthHeader(chain)
        } else {
            try {
                proceedWithAuthHeader(chain, token)
            } catch (e: HttpException) {
                if (e.code() in setOf(401, 403)) {
                    invalidateAuthHeader()
                    proceedWithAuthHeader(chain, getAuthHeader())
                } else {
                    throw e
                }
            }
        }
    }

    private fun proceedWithAuthHeader(chain: Interceptor.Chain, token: String? = null): Response {
        val requestBuilder = chain.request().newBuilder()
        val request = if (token == null) {
            requestBuilder.removeHeader(HEADER_NAME).build()
        } else {
            requestBuilder.header(HEADER_NAME, token).build()
        }
        return chain.proceed(request)
    }

    private fun getAuthHeader() = runBlocking {
        authenticationProvider.get().getAuthHeader()
    }

    private fun invalidateAuthHeader() = runBlocking {
        authenticationProvider.get().invalidateAuthHeader()
    }

    companion object {
        @VisibleForTesting
        const val HEADER_NAME = "Authorization"
    }
}
