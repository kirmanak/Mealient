package gq.kirmanak.mealient.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gq.kirmanak.mealient.data.network.MealieDataSourceWrapper
import gq.kirmanak.mealient.data.recipes.RecipeRepo
import gq.kirmanak.mealient.data.recipes.impl.*
import gq.kirmanak.mealient.data.recipes.network.RecipeDataSource

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
    fun bindRecipePagingSourceFactory(recipePagingSourceFactoryImpl: RecipePagingSourceFactoryImpl): RecipePagingSourceFactory
}