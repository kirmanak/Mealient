package gq.kirmanak.mealient.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gq.kirmanak.mealient.data.add.AddRecipeDataSource
import gq.kirmanak.mealient.data.add.AddRecipeRepo
import gq.kirmanak.mealient.data.add.impl.AddRecipeRepoImpl
import gq.kirmanak.mealient.data.network.MealieDataSourceWrapper
import gq.kirmanak.mealient.datastore.recipe.AddRecipeStorage
import gq.kirmanak.mealient.datastore.recipe.AddRecipeStorageImpl

@Module
@InstallIn(SingletonComponent::class)
interface AddRecipeModule {


    @Binds
    fun provideAddRecipeRepo(repo: AddRecipeRepoImpl): AddRecipeRepo

    @Binds
    fun bindAddRecipeDataSource(mealieDataSourceWrapper: MealieDataSourceWrapper): AddRecipeDataSource

    @Binds
    fun bindAddRecipeStorage(addRecipeStorageImpl: AddRecipeStorageImpl): AddRecipeStorage
}