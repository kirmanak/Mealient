package gq.kirmanak.mealient.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import gq.kirmanak.mealient.data.baseurl.*
import gq.kirmanak.mealient.data.baseurl.impl.BaseUrlLogRedactor
import gq.kirmanak.mealient.data.baseurl.impl.ServerInfoStorageImpl
import gq.kirmanak.mealient.datasource.ServerUrlProvider
import gq.kirmanak.mealient.logging.LogRedactor

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

    @Binds
    @IntoSet
    fun bindBaseUrlLogRedactor(impl: BaseUrlLogRedactor): LogRedactor
}