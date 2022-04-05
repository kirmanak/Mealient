package gq.kirmanak.mealient.data.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import gq.kirmanak.mealient.di.AUTH_OK_HTTP
import gq.kirmanak.mealient.di.NO_AUTH_OK_HTTP
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class RetrofitBuilder @Inject constructor(
    @Named(AUTH_OK_HTTP) private val authOkHttpClient: OkHttpClient,
    @Named(NO_AUTH_OK_HTTP) private val noAuthOkHttpClient: OkHttpClient,
    private val json: Json
) {

    @OptIn(ExperimentalSerializationApi::class)
    fun buildRetrofit(baseUrl: String, needAuth: Boolean): Retrofit {
        Timber.v("buildRetrofit() called with: baseUrl = $baseUrl")
        val contentType = "application/json".toMediaType()
        val converterFactory = json.asConverterFactory(contentType)
        val client = if (needAuth) authOkHttpClient else noAuthOkHttpClient
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(converterFactory)
            .build()
    }
}