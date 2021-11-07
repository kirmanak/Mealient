package gq.kirmanak.mealie.data

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import javax.inject.Inject

class OkHttpBuilder @Inject constructor() {
    fun buildOkHttp(): OkHttpClient {
        return OkHttpClient.Builder()
            .addNetworkInterceptor(buildLoggingInterceptor())
            .build()
    }

    private fun buildLoggingInterceptor() : Interceptor {
        val interceptor = HttpLoggingInterceptor { message -> Timber.v(message) }
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }
}