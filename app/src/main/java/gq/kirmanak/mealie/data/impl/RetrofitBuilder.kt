package gq.kirmanak.mealie.data.impl

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import timber.log.Timber
import javax.inject.Inject

@ExperimentalSerializationApi
class RetrofitBuilder @Inject constructor(private val okHttpBuilder: OkHttpBuilder) {
    fun buildRetrofit(baseUrl: String, token: String? = null): Retrofit {
        Timber.v("buildRetrofit() called with: baseUrl = $baseUrl, token = $token")
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpBuilder.buildOkHttp(token))
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()
    }
}