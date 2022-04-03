package gq.kirmanak.mealient.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gq.kirmanak.mealient.data.auth.AuthDataSource
import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.data.auth.AuthStorage
import gq.kirmanak.mealient.data.auth.impl.AuthDataSourceImpl
import gq.kirmanak.mealient.data.auth.impl.AuthRepoImpl
import gq.kirmanak.mealient.data.auth.impl.AuthService
import gq.kirmanak.mealient.data.auth.impl.AuthStorageImpl
import gq.kirmanak.mealient.data.network.RetrofitBuilder
import gq.kirmanak.mealient.data.network.ServiceFactory
import gq.kirmanak.mealient.data.network.createServiceFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AuthModule {

  companion object {

    @Provides
    @Singleton
    fun provideAuthServiceFactory(retrofitBuilder: RetrofitBuilder): ServiceFactory<AuthService> {
      return retrofitBuilder.createServiceFactory()
    }
  }

  @Binds
  @Singleton
  fun bindAuthDataSource(authDataSourceImpl: AuthDataSourceImpl): AuthDataSource

  @Binds
  @Singleton
  fun bindAuthStorage(authStorageImpl: AuthStorageImpl): AuthStorage

  @Binds
  @Singleton
  fun bindAuthRepo(authRepo: AuthRepoImpl): AuthRepo
}
