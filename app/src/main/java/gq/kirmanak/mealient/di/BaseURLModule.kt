package gq.kirmanak.mealient.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gq.kirmanak.mealient.data.baseurl.*
import gq.kirmanak.mealient.data.baseurl.impl.ServerInfoStorageImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface BaseURLModule {

    @Binds
    @Singleton
    fun bindVersionDataSource(versionDataSourceImpl: VersionDataSourceImpl): VersionDataSource

    @Binds
    @Singleton
    fun bindBaseUrlStorage(baseURLStorageImpl: ServerInfoStorageImpl): ServerInfoStorage

    @Binds
    @Singleton
    fun bindServerInfoRepo(serverInfoRepoImpl: ServerInfoRepoImpl): ServerInfoRepo
}