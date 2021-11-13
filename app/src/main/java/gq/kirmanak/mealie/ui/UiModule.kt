package gq.kirmanak.mealie.ui

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gq.kirmanak.mealie.ui.glide.ImageLoaderGlide

@Module
@InstallIn(SingletonComponent::class)
interface UiModule {
    @Binds
    fun bindImageLoader(imageLoaderGlide: ImageLoaderGlide): ImageLoader
}