package gq.kirmanak.mealient.data.auth.impl

import gq.kirmanak.mealient.data.auth.AuthStorage
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import javax.inject.Inject

const val AUTHORIZATION_HEADER = "Authorization"

class AuthOkHttpInterceptor @Inject constructor(
    private val authStorage: AuthStorage
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        Timber.v("intercept() called with: chain = $chain")
        val token = runBlocking { authStorage.getToken() }
        Timber.d("intercept: token = $token")
        val request = if (token.isNullOrBlank()) {
            chain.request()
        } else {
            chain.request()
                .newBuilder()
                .addHeader(AUTHORIZATION_HEADER, "Bearer $token")
                .build()
        }
        return chain.proceed(request)
    }
}