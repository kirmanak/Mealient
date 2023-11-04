package gq.kirmanak.mealient.datasource.impl

import gq.kirmanak.mealient.logging.Logger
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import javax.inject.Inject

internal class OkHttpBuilderImpl @Inject constructor(
    private val cacheBuilder: CacheBuilderImpl,
    // Use @JvmSuppressWildcards because otherwise dagger can't inject it (https://stackoverflow.com/a/43149382)
    private val interceptors: Set<@JvmSuppressWildcards Interceptor>,
    private val advancedX509TrustManager: AdvancedX509TrustManager,
    private val sslSocketFactoryFactory: SslSocketFactoryFactory,
    private val logger: Logger,
) {

    fun buildOkHttp(): OkHttpClient {
        logger.v { "buildOkHttp() was called with cacheBuilder = $cacheBuilder, interceptors = $interceptors" }

        val sslSocketFactory = sslSocketFactoryFactory.create()

        return OkHttpClient.Builder().apply {
            interceptors.forEach(::addNetworkInterceptor)
            sslSocketFactory(sslSocketFactory, advancedX509TrustManager)
            cache(cacheBuilder.buildCache())
        }.build()
    }
}