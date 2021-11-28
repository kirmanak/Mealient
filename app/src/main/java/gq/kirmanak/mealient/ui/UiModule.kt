package gq.kirmanak.mealient.ui

import com.squareup.picasso.Picasso
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gq.kirmanak.mealient.ui.picasso.ImageLoaderPicasso
import gq.kirmanak.mealient.ui.picasso.PicassoBuilder
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface UiModule {
    @Binds
    fun bindImageLoader(imageLoaderGlide: ImageLoaderPicasso): ImageLoader

    companion object {
        @Provides
        @Singleton
        fun providePicasso(picassoBuilder: PicassoBuilder): Picasso = picassoBuilder.buildPicasso()
    }
}