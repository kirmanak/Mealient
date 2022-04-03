package gq.kirmanak.mealient.data.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import timber.log.Timber
import javax.inject.Inject

class OkHttpBuilder
@Inject
constructor(
    // Use @JvmSuppressWildcards because otherwise dagger can't inject it (https://stackoverflow.com/a/43149382)
    private val interceptors: Set<@JvmSuppressWildcards Interceptor>
) {

    fun buildOkHttp(): OkHttpClient {
        Timber.v("buildOkHttp() called")
        return OkHttpClient.Builder()
            .apply { for (interceptor in interceptors) addNetworkInterceptor(interceptor) }
            .build()
    }
}
