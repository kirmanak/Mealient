package gq.kirmanak.mealient.datasource.impl

import gq.kirmanak.mealient.datasource.CacheBuilder
import gq.kirmanak.mealient.datasource.OkHttpBuilder
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OkHttpBuilderImpl @Inject constructor(
    private val cacheBuilder: CacheBuilder,
    // Use @JvmSuppressWildcards because otherwise dagger can't inject it (https://stackoverflow.com/a/43149382)
    private val interceptors: Set<@JvmSuppressWildcards Interceptor>,
    private val authenticator: Authenticator,
) : OkHttpBuilder {

    override fun buildOkHttp(): OkHttpClient = OkHttpClient.Builder()
        .apply { interceptors.forEach(::addNetworkInterceptor) }
        .cache(cacheBuilder.buildCache())
        .authenticator(authenticator)
        .build()
}