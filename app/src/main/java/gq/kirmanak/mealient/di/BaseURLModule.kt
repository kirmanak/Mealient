package gq.kirmanak.mealient.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gq.kirmanak.mealient.data.baseurl.BaseURLStorage
import gq.kirmanak.mealient.data.baseurl.VersionDataSource
import gq.kirmanak.mealient.data.baseurl.impl.BaseURLStorageImpl
import gq.kirmanak.mealient.data.baseurl.impl.VersionDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface BaseURLModule {

    @Binds
    @Singleton
    fun bindVersionDataSource(versionDataSourceImpl: VersionDataSourceImpl): VersionDataSource

    @Binds
    @Singleton
    fun bindBaseUrlStorage(baseURLStorageImpl: BaseURLStorageImpl): BaseURLStorage
}