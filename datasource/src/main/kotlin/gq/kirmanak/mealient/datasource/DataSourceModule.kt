package gq.kirmanak.mealient.datasource

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import gq.kirmanak.mealient.datasource.impl.AuthInterceptor
import gq.kirmanak.mealient.datasource.impl.BaseUrlInterceptor
import gq.kirmanak.mealient.datasource.impl.CacheBuilderImpl
import gq.kirmanak.mealient.datasource.impl.NetworkRequestWrapperImpl
import gq.kirmanak.mealient.datasource.impl.OkHttpBuilderImpl
import gq.kirmanak.mealient.datasource.impl.RetrofitBuilder
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
        fun provideRetrofit(retrofitBuilder: RetrofitBuilder): Retrofit {
            // Fake base URL which will be replaced later by BaseUrlInterceptor
            // Solution was suggested here https://github.com/square/retrofit/issues/2161#issuecomment-274204152
            return retrofitBuilder.buildRetrofit("http://localhost/")
        }

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
    fun bindCacheBuilder(cacheBuilderImpl: CacheBuilderImpl): CacheBuilder

    @Binds
    fun bindOkHttpBuilder(okHttpBuilderImpl: OkHttpBuilderImpl): OkHttpBuilder

    @Binds
    fun bindMealieDataSource(mealientDataSourceImpl: MealieDataSourceV0Impl): MealieDataSourceV0

    @Binds
    fun bindMealieDataSourceV1(mealientDataSourceImpl: MealieDataSourceV1Impl): MealieDataSourceV1

    @Binds
    fun bindNetworkRequestWrapper(networkRequestWrapperImpl: NetworkRequestWrapperImpl): NetworkRequestWrapper

    @Binds
    @IntoSet
    fun bindAuthInterceptor(authInterceptor: AuthInterceptor): LocalInterceptor

    @Binds
    @IntoSet
    fun bindBaseUrlInterceptor(baseUrlInterceptor: BaseUrlInterceptor): LocalInterceptor
}