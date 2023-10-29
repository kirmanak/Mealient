package gq.kirmanak.mealient.datasource

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import gq.kirmanak.mealient.datasource.impl.AuthInterceptor
import gq.kirmanak.mealient.datasource.impl.BaseUrlInterceptor
import gq.kirmanak.mealient.datasource.impl.CacheBuilderImpl
import gq.kirmanak.mealient.datasource.impl.MealieDataSourceImpl
import gq.kirmanak.mealient.datasource.impl.MealieServiceKtor
import gq.kirmanak.mealient.datasource.impl.NetworkRequestWrapperImpl
import gq.kirmanak.mealient.datasource.impl.OkHttpBuilderImpl
import gq.kirmanak.mealient.datasource.impl.TrustedCertificatesStoreImpl
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
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
        fun provideOkHttp(okHttpBuilder: OkHttpBuilder): OkHttpClient =
            okHttpBuilder.buildOkHttp()
    }

    @Binds
    fun bindCacheBuilder(cacheBuilderImpl: CacheBuilderImpl): CacheBuilder

    @Binds
    fun bindOkHttpBuilder(okHttpBuilderImpl: OkHttpBuilderImpl): OkHttpBuilder

    @Binds
    fun bindMealieDataSource(mealientDataSourceImpl: MealieDataSourceImpl): MealieDataSource

    @Binds
    fun bindMealieService(impl: MealieServiceKtor): MealieService

    @Binds
    fun bindNetworkRequestWrapper(networkRequestWrapperImpl: NetworkRequestWrapperImpl): NetworkRequestWrapper

    @Binds
    @IntoSet
    fun bindAuthInterceptor(authInterceptor: AuthInterceptor): LocalInterceptor

    @Binds
    @IntoSet
    fun bindBaseUrlInterceptor(baseUrlInterceptor: BaseUrlInterceptor): LocalInterceptor

    @Binds
    fun bindTrustedCertificatesStore(impl: TrustedCertificatesStoreImpl): TrustedCertificatesStore
}