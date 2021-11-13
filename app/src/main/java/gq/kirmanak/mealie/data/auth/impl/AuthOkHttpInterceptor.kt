package gq.kirmanak.mealie.data.auth.impl

import okhttp3.Interceptor
import okhttp3.Response

class AuthOkHttpInterceptor(token: String) : Interceptor {
    private val headerValue = "Bearer $token"

    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request()
            .newBuilder()
            .addHeader("Authorization", headerValue)
            .build()
        return chain.proceed(newRequest)
    }
}