package gq.kirmanak.mealient.datasource

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gq.kirmanak.mealient.datasource.v0.MealieDataSourceV0
import gq.kirmanak.mealient.datasource.v0.MealieDataSourceV0Impl
import gq.kirmanak.mealient.datasource.v0.MealieServiceV0
import gq.kirmanak.mealient.datasource.v1.MealieDataSourceV1
import gq.kirmanak.mealient.datasource.v1.MealieDataSourceV1Impl
import gq.kirmanak.mealient.datasource.v1.MealieServiceV1
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {

    companion object {

        const val AUTHORIZATION_HEADER_NAME = "Authorization"

        @Provides
        @Singleton
        fun provideJson(): Json = Json {
            coerceInputValues = true
            ignoreUnknownKeys = true
            encodeDefaults = true
        }

        @OptIn(ExperimentalSerializationApi::class)
        @Provides
        @Singleton
        fun provideConverterFactory(json: Json): Converter.Factory =
            json.asConverterFactory("application/json".toMediaType())

        @Provides
        @Singleton
        fun provideOkHttp(okHttpBuilder: OkHttpBuilder): OkHttpClient =
            okHttpBuilder.buildOkHttp()

        @Provides
        @Singleton
        fun provideRetrofit(retrofitBuilder: RetrofitBuilder): Retrofit =
            retrofitBuilder.buildRetrofit("https://beta.mealie.io/")

        @Provides
        @Singleton
        fun provideMealieService(retrofit: Retrofit): MealieServiceV0 =
            retrofit.create()

        @Provides
        @Singleton
        fun provideMealieServiceV1(retrofit: Retrofit): MealieServiceV1 =
            retrofit.create()
    }

    @Binds
    @Singleton
    fun bindCacheBuilder(cacheBuilderImpl: CacheBuilderImpl): CacheBuilder

    @Binds
    @Singleton
    fun bindOkHttpBuilder(okHttpBuilderImpl: OkHttpBuilderImpl): OkHttpBuilder

    @Binds
    @Singleton
    fun bindMealieDataSource(mealientDataSourceImpl: MealieDataSourceV0Impl): MealieDataSourceV0

    @Binds
    @Singleton
    fun bindMealieDataSourceV1(mealientDataSourceImpl: MealieDataSourceV1Impl): MealieDataSourceV1
}