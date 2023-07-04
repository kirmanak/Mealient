package gq.kirmanak.mealient.datasource.impl

import gq.kirmanak.mealient.datasource.CacheBuilder
import gq.kirmanak.mealient.datasource.LocalInterceptor
import gq.kirmanak.mealient.datasource.OkHttpBuilder
import gq.kirmanak.mealient.logging.Logger
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import javax.inject.Inject

class OkHttpBuilderImpl @Inject constructor(
    private val cacheBuilder: CacheBuilder,
    // Use @JvmSuppressWildcards because otherwise dagger can't inject it (https://stackoverflow.com/a/43149382)
    private val interceptors: Set<@JvmSuppressWildcards Interceptor>,
    private val localInterceptors: Set<@JvmSuppressWildcards LocalInterceptor>,
    private val logger: Logger,
) : OkHttpBuilder {

    override fun buildOkHttp(): OkHttpClient {
        logger.v { "buildOkHttp() was called with cacheBuilder = $cacheBuilder, interceptors = $interceptors, localInterceptors = $localInterceptors" }
        return OkHttpClient.Builder().apply {
            localInterceptors.forEach(::addInterceptor)
            interceptors.forEach(::addNetworkInterceptor)
            cache(cacheBuilder.buildCache())
        }.build()
    }
}