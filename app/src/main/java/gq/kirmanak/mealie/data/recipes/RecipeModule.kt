package gq.kirmanak.mealie.data.recipes

import androidx.paging.ExperimentalPagingApi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import gq.kirmanak.mealie.data.recipes.db.RecipeStorage
import gq.kirmanak.mealie.data.recipes.db.RecipeStorageImpl
import gq.kirmanak.mealie.data.recipes.network.RecipeDataSource
import gq.kirmanak.mealie.data.recipes.network.RecipeDataSourceImpl
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalPagingApi
@ExperimentalSerializationApi
@Module
@InstallIn(ViewModelComponent::class)
interface RecipeModule {
    @Binds
    fun provideRecipeDataSource(recipeDataSourceImpl: RecipeDataSourceImpl): RecipeDataSource

    @Binds
    fun provideRecipeStorage(recipeStorageImpl: RecipeStorageImpl): RecipeStorage

    @Binds
    fun provideRecipeRepo(recipeRepoImpl: RecipeRepoImpl): RecipeRepo

    @Binds
    fun provideRecipeImageLoader(recipeImageLoaderImpl: RecipeImageLoaderImpl): RecipeImageLoader
}