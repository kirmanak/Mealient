package gq.kirmanak.mealient.datasource

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
        fun provideOkHttp(okHttpBuilder: OkHttpBuilderImpl): OkHttpClient =
            okHttpBuilder.buildOkHttp()

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