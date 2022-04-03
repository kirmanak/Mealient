package gq.kirmanak.mealient.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gq.kirmanak.mealient.data.baseurl.*
import gq.kirmanak.mealient.data.network.RetrofitBuilder
import gq.kirmanak.mealient.data.network.ServiceFactory
import gq.kirmanak.mealient.data.network.createServiceFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface BaseURLModule {

    companion object {

        @Provides
        @Singleton
        fun provideVersionServiceFactory(
            retrofitBuilder: RetrofitBuilder,
            baseURLStorage: BaseURLStorage,
        ): ServiceFactory<VersionService> = retrofitBuilder.createServiceFactory(baseURLStorage)
    }

    @Binds
    @Singleton
    fun bindVersionDataSource(versionDataSourceImpl: VersionDataSourceImpl): VersionDataSource

    @Binds
    @Singleton
    fun bindBaseUrlStorage(baseURLStorageImpl: BaseURLStorageImpl): BaseURLStorage
}