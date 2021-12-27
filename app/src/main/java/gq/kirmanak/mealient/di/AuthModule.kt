package gq.kirmanak.mealient.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import gq.kirmanak.mealient.data.auth.AuthDataSource
import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.data.auth.AuthStorage
import gq.kirmanak.mealient.data.auth.impl.AuthDataSourceImpl
import gq.kirmanak.mealient.data.auth.impl.AuthOkHttpInterceptor
import gq.kirmanak.mealient.data.auth.impl.AuthRepoImpl
import gq.kirmanak.mealient.data.auth.impl.AuthStorageImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.Interceptor

@ExperimentalCoroutinesApi
@ExperimentalSerializationApi
@Module
@InstallIn(SingletonComponent::class)
interface AuthModule {
  @Binds
  fun bindAuthDataSource(authDataSourceImpl: AuthDataSourceImpl): AuthDataSource

  @Binds
  fun bindAuthStorage(authStorageImpl: AuthStorageImpl): AuthStorage

  @Binds
  fun bindAuthRepo(authRepo: AuthRepoImpl): AuthRepo

  @Binds
  @IntoSet
  fun bindAuthInterceptor(authOkHttpInterceptor: AuthOkHttpInterceptor): Interceptor
}
