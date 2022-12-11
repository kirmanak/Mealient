package gq.kirmanak.mealient.datasource.impl

import gq.kirmanak.mealient.logging.Logger
import okhttp3.OkHttpClient
import retrofit2.Converter.Factory
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitBuilder @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val converterFactory: Factory,
    private val logger: Logger,
) {

    fun buildRetrofit(baseUrl: String): Retrofit {
        logger.v { "buildRetrofit() called with: baseUrl = $baseUrl" }
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }
}