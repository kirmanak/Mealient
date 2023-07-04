package gq.kirmanak.mealient.datasource.impl

import androidx.annotation.VisibleForTesting
import gq.kirmanak.mealient.datasource.AuthenticationProvider
import gq.kirmanak.mealient.datasource.LocalInterceptor
import gq.kirmanak.mealient.logging.Logger
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Provider

class AuthInterceptor @Inject constructor(
    private val logger: Logger,
    private val authenticationProviderProvider: Provider<AuthenticationProvider>,
) : LocalInterceptor {

    private val authenticationProvider: AuthenticationProvider
        get() = authenticationProviderProvider.get()

    override fun intercept(chain: Interceptor.Chain): Response {
        logger.v { "intercept() was called with: request = ${chain.request()}" }
        val header = getAuthHeader()
        val request = chain.request().let {
            if (header == null) it else it.newBuilder().header(HEADER_NAME, header).build()
        }
        logger.d { "Sending header $HEADER_NAME=${request.header(HEADER_NAME)}" }
        return chain.proceed(request).also {
            logger.v { "Response code is ${it.code}" }
            if (it.code == 401 && header != null) logout()
        }
    }

    private fun getAuthHeader() = runBlocking { authenticationProvider.getAuthHeader() }

    private fun logout() = runBlocking { authenticationProvider.logout() }

    companion object {
        @VisibleForTesting
        const val HEADER_NAME = "Authorization"
    }
}