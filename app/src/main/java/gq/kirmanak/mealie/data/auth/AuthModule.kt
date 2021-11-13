package gq.kirmanak.mealie.data.auth

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import gq.kirmanak.mealie.data.auth.impl.AuthDataSourceImpl
import gq.kirmanak.mealie.data.auth.impl.AuthRepoImpl
import gq.kirmanak.mealie.data.auth.impl.AuthStorageImpl
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
@Module
@InstallIn(ViewModelComponent::class)
abstract class AuthModule {
    @Binds
    abstract fun bindAuthDataSource(authDataSourceImpl: AuthDataSourceImpl): AuthDataSource

    @Binds
    abstract fun bindAuthStorage(authStorageImpl: AuthStorageImpl): AuthStorage

    @Binds
    abstract fun bindAuthRepo(authRepo: AuthRepoImpl): AuthRepo
}