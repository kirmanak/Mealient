package gq.kirmanak.mealient.datasource.impl

import gq.kirmanak.mealient.datasource.AuthenticationProvider
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val authenticationProvider: Provider<AuthenticationProvider>,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { authenticationProvider.get().getAuthHeader() }
        val request = if (token == null) {
            chain.request()
        } else {
            chain.request()
                .newBuilder()
                .header(HEADER_NAME, token)
                .build()
        }
        return chain.proceed(request)
    }

    companion object {
        private const val HEADER_NAME = "Authorization"
    }
}
