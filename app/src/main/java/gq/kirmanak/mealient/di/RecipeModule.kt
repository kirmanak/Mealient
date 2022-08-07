package gq.kirmanak.mealient.di

import androidx.paging.InvalidatingPagingSourceFactory
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
import gq.kirmanak.mealient.data.recipes.db.RecipeStorage
import gq.kirmanak.mealient.data.recipes.db.RecipeStorageImpl
import gq.kirmanak.mealient.data.recipes.impl.RecipeImageUrlProvider
import gq.kirmanak.mealient.data.recipes.impl.RecipeImageUrlProviderImpl
import gq.kirmanak.mealient.data.recipes.impl.RecipeRepoImpl
import gq.kirmanak.mealient.data.recipes.network.RecipeDataSource
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.ui.recipes.images.RecipeModelLoaderFactory
import java.io.InputStream
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RecipeModule {

    @Binds
    @Singleton
    fun provideRecipeDataSource(mealieDataSourceWrapper: MealieDataSourceWrapper): RecipeDataSource

    @Binds
    @Singleton
    fun provideRecipeStorage(recipeStorageImpl: RecipeStorageImpl): RecipeStorage

    @Binds
    @Singleton
    fun provideRecipeRepo(recipeRepoImpl: RecipeRepoImpl): RecipeRepo

    @Binds
    @Singleton
    fun bindImageUrlProvider(recipeImageUrlProviderImpl: RecipeImageUrlProviderImpl): RecipeImageUrlProvider

    @Binds
    @Singleton
    fun bindModelLoaderFactory(recipeModelLoaderFactory: RecipeModelLoaderFactory): ModelLoaderFactory<RecipeSummaryEntity, InputStream>

    companion object {

        @Provides
        @Singleton
        fun provideRecipePagingSourceFactory(
            recipeStorage: RecipeStorage
        ) = InvalidatingPagingSourceFactory { recipeStorage.queryRecipes() }

        @Provides
        @Singleton
        fun provideGlideRequestOptions(): RequestOptions = RequestOptions.centerCropTransform()
            .placeholder(R.drawable.placeholder_recipe)
    }
}