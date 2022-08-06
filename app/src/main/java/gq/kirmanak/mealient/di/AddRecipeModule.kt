package gq.kirmanak.mealient.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gq.kirmanak.mealient.data.add.AddRecipeDataSource
import gq.kirmanak.mealient.data.add.AddRecipeRepo
import gq.kirmanak.mealient.data.add.impl.AddRecipeDataSourceImpl
import gq.kirmanak.mealient.data.add.impl.AddRecipeRepoImpl
import gq.kirmanak.mealient.datastore.recipe.AddRecipeStorage
import gq.kirmanak.mealient.datastore.recipe.AddRecipeStorageImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AddRecipeModule {


    @Binds
    @Singleton
    fun provideAddRecipeRepo(repo: AddRecipeRepoImpl): AddRecipeRepo

    @Binds
    @Singleton
    fun bindAddRecipeDataSource(addRecipeDataSourceImpl: AddRecipeDataSourceImpl): AddRecipeDataSource

    @Binds
    @Singleton
    fun bindAddRecipeStorage(addRecipeStorageImpl: AddRecipeStorageImpl): AddRecipeStorage
}