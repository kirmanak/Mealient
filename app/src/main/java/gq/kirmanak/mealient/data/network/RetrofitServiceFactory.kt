package gq.kirmanak.mealient.data.network

import gq.kirmanak.mealient.data.baseurl.BaseURLStorage
import timber.log.Timber

inline fun <reified T> RetrofitBuilder.createServiceFactory(baseURLStorage: BaseURLStorage) =
    RetrofitServiceFactory(T::class.java, this, baseURLStorage)

class RetrofitServiceFactory<T>(
    private val serviceClass: Class<T>,
    private val retrofitBuilder: RetrofitBuilder,
    private val baseURLStorage: BaseURLStorage,
) : ServiceFactory<T> {

    private val cache: MutableMap<String, T> = mutableMapOf()

    override suspend fun provideService(baseUrl: String?): T {
        Timber.v("provideService() called with: baseUrl = $baseUrl, class = ${serviceClass.simpleName}")
        val url = baseUrl ?: baseURLStorage.requireBaseURL()
        return synchronized(cache) { cache[url] ?: createService(url, serviceClass) }
    }

    private fun createService(url: String, serviceClass: Class<T>): T {
        Timber.v("createService() called with: url = $url, serviceClass = ${serviceClass.simpleName}")
        val service = retrofitBuilder.buildRetrofit(url).create(serviceClass)
        cache[url] = service
        return service
    }
}