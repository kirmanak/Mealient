package gq.kirmanak.mealient.data.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import timber.log.Timber

class RetrofitBuilder(
    private val okHttpClient: OkHttpClient,
    private val json: Json
) {

    @OptIn(ExperimentalSerializationApi::class)
    fun buildRetrofit(baseUrl: String): Retrofit {
        Timber.v("buildRetrofit() called with: baseUrl = $baseUrl")
        val contentType = "application/json".toMediaType()
        val converterFactory = json.asConverterFactory(contentType)
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }
}