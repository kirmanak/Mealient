package gq.kirmanak.mealient.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped
import gq.kirmanak.mealient.ui.recipes.images.RecipeImageLoader
import gq.kirmanak.mealient.ui.recipes.images.RecipeImageLoaderImpl
import gq.kirmanak.mealient.ui.recipes.images.RecipePreloaderFactory
import gq.kirmanak.mealient.ui.recipes.images.RecipePreloaderFactoryImpl

@Module
@InstallIn(FragmentComponent::class)
interface UiModule {

    @Binds
    @FragmentScoped
    fun provideRecipeImageLoader(recipeImageLoaderImpl: RecipeImageLoaderImpl): RecipeImageLoader

    @Binds
    @FragmentScoped
    fun bindRecipePreloaderFactory(recipePreloaderFactoryImpl: RecipePreloaderFactoryImpl): RecipePreloaderFactory

}