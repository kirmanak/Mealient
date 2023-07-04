package gq.kirmanak.mealient.di

import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.request.RequestOptions
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.data.network.MealieDataSourceWrapper
import gq.kirmanak.mealient.data.recipes.RecipeRepo
import gq.kirmanak.mealient.data.recipes.impl.*
import gq.kirmanak.mealient.data.recipes.network.RecipeDataSource
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.ui.recipes.images.RecipeModelLoaderFactory
import java.io.InputStream

@Module
@InstallIn(SingletonComponent::class)
interface RecipeModule {

    @Binds
    fun provideRecipeDataSource(mealieDataSourceWrapper: MealieDataSourceWrapper): RecipeDataSource

    @Binds
    fun provideRecipeRepo(recipeRepoImpl: RecipeRepoImpl): RecipeRepo

    @Binds
    fun bindImageUrlProvider(recipeImageUrlProviderImpl: RecipeImageUrlProviderImpl): RecipeImageUrlProvider

    @Binds
    fun bindModelLoaderFactory(recipeModelLoaderFactory: RecipeModelLoaderFactory): ModelLoaderFactory<RecipeSummaryEntity, InputStream>

    @Binds
    fun bindRecipePagingSourceFactory(recipePagingSourceFactoryImpl: RecipePagingSourceFactoryImpl): RecipePagingSourceFactory

    companion object {

        @Provides
        fun provideGlideRequestOptions(): RequestOptions = RequestOptions.centerCropTransform()
            .placeholder(R.drawable.placeholder_recipe)
    }
}