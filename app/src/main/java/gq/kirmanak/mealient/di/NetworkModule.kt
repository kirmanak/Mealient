package gq.kirmanak.mealient.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gq.kirmanak.mealient.data.network.OkHttpBuilder
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun createOkHttp(okHttpBuilder: OkHttpBuilder): OkHttpClient =
        okHttpBuilder.buildOkHttp()

    @Provides
    @Singleton
    fun createJson(): Json = Json {
        coerceInputValues = true
        ignoreUnknownKeys = true
    }
}