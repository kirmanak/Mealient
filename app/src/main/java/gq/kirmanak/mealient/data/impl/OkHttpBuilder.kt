package gq.kirmanak.mealient.data.impl

import com.facebook.stetho.okhttp3.StethoInterceptor
import gq.kirmanak.mealient.BuildConfig
import gq.kirmanak.mealient.data.auth.impl.AuthOkHttpInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import javax.inject.Inject

class OkHttpBuilder @Inject constructor(
    private val authOkHttpInterceptor: AuthOkHttpInterceptor
) {
    fun buildOkHttp(): OkHttpClient {
        Timber.v("buildOkHttp() called")
        val builder = OkHttpClient.Builder()
        builder.addNetworkInterceptor(authOkHttpInterceptor)
        if (BuildConfig.DEBUG) {
            builder.addNetworkInterceptor(buildLoggingInterceptor())
            builder.addNetworkInterceptor(StethoInterceptor())
        }
        return builder.build()
    }

    private fun buildLoggingInterceptor(): Interceptor {
        val interceptor = HttpLoggingInterceptor { message -> Timber.tag("OkHttp").v(message) }
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }
}