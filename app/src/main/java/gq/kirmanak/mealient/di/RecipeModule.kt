package gq.kirmanak.mealient.di

import androidx.paging.InvalidatingPagingSourceFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gq.kirmanak.mealient.data.baseurl.BaseURLStorage
import gq.kirmanak.mealient.data.network.RetrofitBuilder
import gq.kirmanak.mealient.data.network.ServiceFactory
import gq.kirmanak.mealient.data.network.createServiceFactory
import gq.kirmanak.mealient.data.recipes.RecipeImageLoader
import gq.kirmanak.mealient.data.recipes.RecipeRepo
import gq.kirmanak.mealient.data.recipes.db.RecipeStorage
import gq.kirmanak.mealient.data.recipes.db.RecipeStorageImpl
import gq.kirmanak.mealient.data.recipes.impl.RecipeImageLoaderImpl
import gq.kirmanak.mealient.data.recipes.impl.RecipeRepoImpl
import gq.kirmanak.mealient.data.recipes.network.RecipeDataSource
import gq.kirmanak.mealient.data.recipes.network.RecipeDataSourceImpl
import gq.kirmanak.mealient.data.recipes.network.RecipeService
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RecipeModule {

    @Binds
    @Singleton
    fun provideRecipeDataSource(recipeDataSourceImpl: RecipeDataSourceImpl): RecipeDataSource

    @Binds
    @Singleton
    fun provideRecipeStorage(recipeStorageImpl: RecipeStorageImpl): RecipeStorage

    @Binds
    @Singleton
    fun provideRecipeRepo(recipeRepoImpl: RecipeRepoImpl): RecipeRepo

    @Binds
    @Singleton
    fun provideRecipeImageLoader(recipeImageLoaderImpl: RecipeImageLoaderImpl): RecipeImageLoader

    companion object {

        @Provides
        @Singleton
        fun provideRecipeServiceFactory(
            @Named(AUTH_OK_HTTP) okHttpClient: OkHttpClient,
            json: Json,
            baseURLStorage: BaseURLStorage,
        ): ServiceFactory<RecipeService> {
            return RetrofitBuilder(okHttpClient, json).createServiceFactory(baseURLStorage)
        }

        @Provides
        @Singleton
        fun provideRecipePagingSourceFactory(
            recipeStorage: RecipeStorage
        ) = InvalidatingPagingSourceFactory { recipeStorage.queryRecipes() }
    }
}