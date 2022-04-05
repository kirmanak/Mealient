package gq.kirmanak.mealient.data.network

import gq.kirmanak.mealient.data.baseurl.BaseURLStorage
import gq.kirmanak.mealient.extensions.runCatchingExceptCancel
import timber.log.Timber

inline fun <reified T> RetrofitBuilder.createServiceFactory(baseURLStorage: BaseURLStorage) =
    RetrofitServiceFactory(T::class.java, this, baseURLStorage)

class RetrofitServiceFactory<T>(
    private val serviceClass: Class<T>,
    private val retrofitBuilder: RetrofitBuilder,
    private val baseURLStorage: BaseURLStorage,
) : ServiceFactory<T> {

    private val cache: MutableMap<String, T> = mutableMapOf()

    override suspend fun provideService(baseUrl: String?): T = runCatchingExceptCancel {
        Timber.v("provideService() called with: baseUrl = $baseUrl, class = ${serviceClass.simpleName}")
        val url = baseUrl ?: baseURLStorage.requireBaseURL()
        synchronized(cache) { cache[url] ?: createService(url, serviceClass) }
    }.getOrElse {
        Timber.e(it, "provideService: can't provide service for $baseUrl")
        throw NetworkError.MalformedUrl(it)
    }

    private fun createService(url: String, serviceClass: Class<T>): T {
        Timber.v("createService() called with: url = $url, serviceClass = ${serviceClass.simpleName}")
        val service = retrofitBuilder.buildRetrofit(url).create(serviceClass)
        cache[url] = service
        return service
    }
}