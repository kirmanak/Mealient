package gq.kirmanak.mealient.data.network

import gq.kirmanak.mealient.data.baseurl.BaseURLStorage
import gq.kirmanak.mealient.extensions.runCatchingExceptCancel
import gq.kirmanak.mealient.logging.Logger

inline fun <reified T> RetrofitBuilder.createServiceFactory(
    baseURLStorage: BaseURLStorage,
    logger: Logger
) =
    RetrofitServiceFactory(T::class.java, this, baseURLStorage, logger)

class RetrofitServiceFactory<T>(
    private val serviceClass: Class<T>,
    private val retrofitBuilder: RetrofitBuilder,
    private val baseURLStorage: BaseURLStorage,
    private val logger: Logger,
) : ServiceFactory<T> {

    private val cache: MutableMap<String, T> = mutableMapOf()

    override suspend fun provideService(baseUrl: String?): T = runCatchingExceptCancel {
        logger.v { "provideService() called with: baseUrl = $baseUrl, class = ${serviceClass.simpleName}" }
        val url = baseUrl ?: baseURLStorage.requireBaseURL()
        synchronized(cache) { cache[url] ?: createService(url, serviceClass) }
    }.getOrElse {
        logger.e(it) { "provideService: can't provide service for $baseUrl" }
        throw NetworkError.MalformedUrl(it)
    }

    private fun createService(url: String, serviceClass: Class<T>): T {
        logger.v { "createService() called with: url = $url, serviceClass = ${serviceClass.simpleName}" }
        val service = retrofitBuilder.buildRetrofit(url).create(serviceClass)
        cache[url] = service
        return service
    }
}