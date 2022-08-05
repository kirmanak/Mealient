package gq.kirmanak.mealient.data.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import gq.kirmanak.mealient.logging.Logger
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class RetrofitBuilder(
    private val okHttpClient: OkHttpClient,
    private val json: Json,
    private val logger: Logger,
) {

    @OptIn(ExperimentalSerializationApi::class)
    fun buildRetrofit(baseUrl: String): Retrofit {
        logger.v { "buildRetrofit() called with: baseUrl = $baseUrl" }
        val contentType = "application/json".toMediaType()
        val converterFactory = json.asConverterFactory(contentType)
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }
}