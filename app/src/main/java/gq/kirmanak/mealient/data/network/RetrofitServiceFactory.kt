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

    private val cache: MutableMap<ServiceParams, T> = mutableMapOf()

    override suspend fun provideService(
        baseUrl: String?,
        needAuth: Boolean,
    ): T = runCatchingExceptCancel {
        Timber.v("provideService() called with: baseUrl = $baseUrl, class = ${serviceClass.simpleName}")
        val url = baseUrl ?: baseURLStorage.requireBaseURL()
        val params = ServiceParams(url, needAuth)
        synchronized(cache) { cache[params] ?: createService(params, serviceClass) }
    }.getOrElse {
        Timber.e(it, "provideService: can't provide service for $baseUrl")
        throw NetworkError.MalformedUrl(it)
    }

    private fun createService(serviceParams: ServiceParams, serviceClass: Class<T>): T {
        Timber.v("createService() called with: serviceParams = $serviceParams, serviceClass = ${serviceClass.simpleName}")
        val (url, needAuth) = serviceParams
        val service = retrofitBuilder.buildRetrofit(url, needAuth).create(serviceClass)
        cache[serviceParams] = service
        return service
    }

    data class ServiceParams(val baseUrl: String, val needAuth: Boolean)
}