package gq.kirmanak.mealient.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gq.kirmanak.mealient.data.network.AuthenticationInterceptor
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import javax.inject.Named
import javax.inject.Singleton

const val AUTH_OK_HTTP = "auth"
const val NO_AUTH_OK_HTTP = "noauth"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @Named(AUTH_OK_HTTP)
    fun createAuthOkHttp(
        // Use @JvmSuppressWildcards because otherwise dagger can't inject it (https://stackoverflow.com/a/43149382)
        interceptors: Set<@JvmSuppressWildcards Interceptor>,
        authenticationInterceptor: AuthenticationInterceptor,
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authenticationInterceptor)
        .apply { for (interceptor in interceptors) addNetworkInterceptor(interceptor) }
        .build()

    @Provides
    @Singleton
    @Named(NO_AUTH_OK_HTTP)
    fun createNoAuthOkHttp(
        // Use @JvmSuppressWildcards because otherwise dagger can't inject it (https://stackoverflow.com/a/43149382)
        interceptors: Set<@JvmSuppressWildcards Interceptor>,
    ): OkHttpClient = OkHttpClient.Builder()
        .apply { for (interceptor in interceptors) addNetworkInterceptor(interceptor) }
        .build()

    @Provides
    @Singleton
    fun createJson(): Json = Json {
        coerceInputValues = true
        ignoreUnknownKeys = true
        encodeDefaults = true
    }
}