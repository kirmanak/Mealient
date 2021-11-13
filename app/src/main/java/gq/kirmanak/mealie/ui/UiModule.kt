package gq.kirmanak.mealie.ui

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface UiModule {
    @Binds
    fun bindImageLoader(imageLoaderGlide: ImageLoaderGlide): ImageLoader
}