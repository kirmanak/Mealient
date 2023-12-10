package gq.kirmanak.mealient.datasource

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import gq.kirmanak.mealient.datasource.impl.MealieDataSourceImpl
import gq.kirmanak.mealient.datasource.impl.MealieServiceKtor
import gq.kirmanak.mealient.datasource.impl.NetworkRequestWrapperImpl
import gq.kirmanak.mealient.datasource.impl.OkHttpBuilderImpl
import gq.kirmanak.mealient.datasource.impl.TrustedCertificatesStoreImpl
import gq.kirmanak.mealient.logging.Logger
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface DataSourceModule {

    companion object {

        @Provides
        @Singleton
        fun provideJson(): Json = Json {
            coerceInputValues = true
            ignoreUnknownKeys = true
            encodeDefaults = true
        }

        @Provides
        @Singleton
        fun provideOkHttp(okHttpBuilder: OkHttpBuilderImpl): OkHttpClient =
            okHttpBuilder.buildOkHttp()

        @Provides
        @IntoSet
        fun provideLoggingInterceptor(logger: Logger): Interceptor {
            val interceptor =
                HttpLoggingInterceptor { message -> logger.v(tag = "OkHttp") { message } }
            interceptor.level = when {
                BuildConfig.LOG_NETWORK -> HttpLoggingInterceptor.Level.BODY
                else -> HttpLoggingInterceptor.Level.BASIC
            }
            return interceptor
        }
    }

    @Binds
    fun bindMealieDataSource(mealientDataSourceImpl: MealieDataSourceImpl): MealieDataSource

    @Binds
    fun bindMealieService(impl: MealieServiceKtor): MealieService

    @Binds
    fun bindNetworkRequestWrapper(networkRequestWrapperImpl: NetworkRequestWrapperImpl): NetworkRequestWrapper

    @Binds
    fun bindTrustedCertificatesStore(impl: TrustedCertificatesStoreImpl): TrustedCertificatesStore
}