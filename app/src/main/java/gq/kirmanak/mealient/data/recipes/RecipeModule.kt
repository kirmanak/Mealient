package gq.kirmanak.mealient.data.recipes

import androidx.paging.ExperimentalPagingApi
import androidx.paging.InvalidatingPagingSourceFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gq.kirmanak.mealient.data.recipes.db.RecipeStorage
import gq.kirmanak.mealient.data.recipes.db.RecipeStorageImpl
import gq.kirmanak.mealient.data.recipes.impl.RecipeImageLoaderImpl
import gq.kirmanak.mealient.data.recipes.impl.RecipeRepoImpl
import gq.kirmanak.mealient.data.recipes.network.RecipeDataSource
import gq.kirmanak.mealient.data.recipes.network.RecipeDataSourceImpl
import kotlinx.serialization.ExperimentalSerializationApi
import javax.inject.Singleton

@ExperimentalPagingApi
@ExperimentalSerializationApi
@Module
@InstallIn(SingletonComponent::class)
interface RecipeModule {
    @Binds
    fun provideRecipeDataSource(recipeDataSourceImpl: RecipeDataSourceImpl): RecipeDataSource

    @Binds
    fun provideRecipeStorage(recipeStorageImpl: RecipeStorageImpl): RecipeStorage

    @Binds
    fun provideRecipeRepo(recipeRepoImpl: RecipeRepoImpl): RecipeRepo

    @Binds
    fun provideRecipeImageLoader(recipeImageLoaderImpl: RecipeImageLoaderImpl): RecipeImageLoader

    companion object {
        @Provides
        @Singleton
        fun provideRecipePagingSourceFactory(
            recipeStorage: RecipeStorage
        ) = InvalidatingPagingSourceFactory { recipeStorage.queryRecipes() }
    }
}