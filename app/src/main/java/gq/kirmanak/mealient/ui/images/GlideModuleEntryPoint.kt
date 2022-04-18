package gq.kirmanak.mealient.ui.images

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gq.kirmanak.mealient.di.AUTH_OK_HTTP
import okhttp3.OkHttpClient
import javax.inject.Named
import javax.inject.Singleton

@EntryPoint
@InstallIn(SingletonComponent::class)
interface GlideModuleEntryPoint {

    @Singleton
    @Named(AUTH_OK_HTTP)
    fun provideOkHttp(): OkHttpClient
}