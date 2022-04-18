package gq.kirmanak.mealient.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped
import gq.kirmanak.mealient.data.recipes.impl.RecipeImageLoaderImpl
import gq.kirmanak.mealient.ui.images.ImageLoader
import gq.kirmanak.mealient.ui.images.ImageLoaderGlide
import gq.kirmanak.mealient.ui.recipes.RecipeImageLoader

@Module
@InstallIn(FragmentComponent::class)
interface UiModule {

    @Binds
    @FragmentScoped
    fun bindImageLoader(imageLoaderGlide: ImageLoaderGlide): ImageLoader

    @Binds
    @FragmentScoped
    fun provideRecipeImageLoader(recipeImageLoaderImpl: RecipeImageLoaderImpl): RecipeImageLoader

}