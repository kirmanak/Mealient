package gq.kirmanak.mealient.di

import com.bumptech.glide.load.model.ModelLoaderFactory
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gq.kirmanak.mealient.data.recipes.db.entity.RecipeSummaryEntity
import okhttp3.OkHttpClient
import java.io.InputStream
import javax.inject.Named

@EntryPoint
@InstallIn(SingletonComponent::class)
interface GlideModuleEntryPoint {

    @Named(AUTH_OK_HTTP)
    fun provideOkHttp(): OkHttpClient

    fun provideRecipeLoaderFactory(): ModelLoaderFactory<RecipeSummaryEntity, InputStream>
}