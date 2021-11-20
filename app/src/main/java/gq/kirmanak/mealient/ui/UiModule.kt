package gq.kirmanak.mealient.ui

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gq.kirmanak.mealient.ui.glide.ImageLoaderGlide

@Module
@InstallIn(SingletonComponent::class)
interface UiModule {
    @Binds
    fun bindImageLoader(imageLoaderGlide: ImageLoaderGlide): ImageLoader
}