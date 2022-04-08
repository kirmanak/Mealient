package gq.kirmanak.mealient.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gq.kirmanak.mealient.data.baseurl.BaseURLStorage
import gq.kirmanak.mealient.data.baseurl.VersionDataSource
import gq.kirmanak.mealient.data.baseurl.impl.BaseURLStorageImpl
import gq.kirmanak.mealient.data.baseurl.impl.VersionDataSourceImpl
import gq.kirmanak.mealient.data.baseurl.impl.VersionService
import gq.kirmanak.mealient.data.network.RetrofitBuilder
import gq.kirmanak.mealient.data.network.ServiceFactory
import gq.kirmanak.mealient.data.network.createServiceFactory
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface BaseURLModule {

    companion object {

        @Provides
        @Singleton
        fun provideVersionServiceFactory(
            @Named(AUTH_OK_HTTP) okHttpClient: OkHttpClient,
            json: Json,
            baseURLStorage: BaseURLStorage,
        ): ServiceFactory<VersionService> {
            return RetrofitBuilder(okHttpClient, json).createServiceFactory(baseURLStorage)
        }
    }

    @Binds
    @Singleton
    fun bindVersionDataSource(versionDataSourceImpl: VersionDataSourceImpl): VersionDataSource

    @Binds
    @Singleton
    fun bindBaseUrlStorage(baseURLStorageImpl: BaseURLStorageImpl): BaseURLStorage
}