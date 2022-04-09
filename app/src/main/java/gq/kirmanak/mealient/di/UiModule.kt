package gq.kirmanak.mealient.di

import com.squareup.picasso.Picasso
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gq.kirmanak.mealient.ui.images.ImageLoader
import gq.kirmanak.mealient.ui.images.ImageLoaderPicasso
import gq.kirmanak.mealient.ui.images.PicassoBuilder
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface UiModule {

    @Binds
    @Singleton
    fun bindImageLoader(imageLoaderGlide: ImageLoaderPicasso): ImageLoader

    companion object {
        @Provides
        @Singleton
        fun providePicasso(picassoBuilder: PicassoBuilder): Picasso = picassoBuilder.buildPicasso()
    }
}