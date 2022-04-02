package gq.kirmanak.mealient.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ReleaseModule {

  // Release version of the application doesn't have any interceptors but this Set
  // is required by Dagger, so an empty Set is provided here
  // Use @JvmSuppressWildcards because otherwise dagger can't inject it (https://stackoverflow.com/a/43149382)
  @Provides
  @Singleton
  fun provideInterceptors(): Set<@JvmSuppressWildcards Interceptor> = emptySet()
}
