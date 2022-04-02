package gq.kirmanak.mealient.data.network

import gq.kirmanak.mealient.data.impl.RetrofitBuilder
import timber.log.Timber

inline fun <reified T> RetrofitBuilder.createServiceFactory() =
    RetrofitServiceFactory(T::class.java, this)

class RetrofitServiceFactory<T>(
    private val serviceClass: Class<T>,
    private val retrofitBuilder: RetrofitBuilder,
) : ServiceFactory<T> {

    private val cache: MutableMap<String, T> = mutableMapOf()

    @Synchronized
    override fun provideService(baseUrl: String): T {
        Timber.v("provideService() called with: baseUrl = $baseUrl, class = ${serviceClass.simpleName}")
        val cached = cache[baseUrl]
        return if (cached == null) {
            Timber.d("provideService: cache is empty, creating new")
            val new = retrofitBuilder.buildRetrofit(baseUrl).create(serviceClass)
            cache[baseUrl] = new
            new
        } else {
            cached
        }
    }
}