package gq.kirmanak.mealient.data.auth.impl

import okhttp3.Interceptor
import okhttp3.Response

const val AUTHORIZATION_HEADER = "Authorization"

class AuthOkHttpInterceptor(token: String) : Interceptor {
    private val headerValue = "Bearer $token"

    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request()
            .newBuilder()
            .addHeader(AUTHORIZATION_HEADER, headerValue)
            .build()
        return chain.proceed(newRequest)
    }
}