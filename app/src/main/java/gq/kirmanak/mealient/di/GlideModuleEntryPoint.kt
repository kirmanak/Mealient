package gq.kirmanak.mealient.di

import com.bumptech.glide.load.model.ModelLoaderFactory
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.logging.Logger
import okhttp3.OkHttpClient
import java.io.InputStream
import javax.inject.Named

@EntryPoint
@InstallIn(SingletonComponent::class)
interface GlideModuleEntryPoint {

    fun provideLogger(): Logger

    @Named(AUTH_OK_HTTP)
    fun provideOkHttp(): OkHttpClient

    fun provideRecipeLoaderFactory(): ModelLoaderFactory<RecipeSummaryEntity, InputStream>
}