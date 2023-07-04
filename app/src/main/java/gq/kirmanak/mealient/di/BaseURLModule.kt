package gq.kirmanak.mealient.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gq.kirmanak.mealient.data.baseurl.*
import gq.kirmanak.mealient.data.baseurl.impl.ServerInfoStorageImpl
import gq.kirmanak.mealient.datasource.ServerUrlProvider

@Module
@InstallIn(SingletonComponent::class)
interface BaseURLModule {

    @Binds
    fun bindVersionDataSource(versionDataSourceImpl: VersionDataSourceImpl): VersionDataSource

    @Binds
    fun bindBaseUrlStorage(baseURLStorageImpl: ServerInfoStorageImpl): ServerInfoStorage

    @Binds
    fun bindServerInfoRepo(serverInfoRepoImpl: ServerInfoRepoImpl): ServerInfoRepo

    @Binds
    fun bindServerUrlProvider(serverInfoRepoImpl: ServerInfoRepoImpl): ServerUrlProvider
}