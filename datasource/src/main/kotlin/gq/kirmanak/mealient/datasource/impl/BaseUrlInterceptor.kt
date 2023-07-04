package gq.kirmanak.mealient.datasource.impl

import gq.kirmanak.mealient.datasource.LocalInterceptor
import gq.kirmanak.mealient.datasource.ServerUrlProvider
import gq.kirmanak.mealient.logging.Logger
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Provider

internal class BaseUrlInterceptor @Inject constructor(
    private val logger: Logger,
    private val serverUrlProviderProvider: Provider<ServerUrlProvider>,
) : LocalInterceptor {

    private val serverUrlProvider: ServerUrlProvider
        get() = serverUrlProviderProvider.get()

    override fun intercept(chain: Interceptor.Chain): Response {
        logger.v { "intercept() was called with: request = ${chain.request()}" }
        val oldRequest = chain.request()
        val baseUrl = getBaseUrl()
        val correctUrl = oldRequest.url
            .newBuilder()
            .host(baseUrl.host)
            .scheme(baseUrl.scheme)
            .port(baseUrl.port)
            .build()
        val newRequest = oldRequest.newBuilder().url(correctUrl).build()
        logger.d { "Replaced ${oldRequest.url} with ${newRequest.url}" }
        return chain.proceed(newRequest)
    }

    private fun getBaseUrl() = runBlocking {
        val url = serverUrlProvider.getUrl() ?: throw IOException("Base URL is unknown")
        url.runCatching {
            toHttpUrl()
        }.fold(
            onSuccess = { it },
            onFailure = { throw IOException(it.message, it) },
        )
    }
}