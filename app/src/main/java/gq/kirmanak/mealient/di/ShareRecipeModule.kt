package gq.kirmanak.mealient.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gq.kirmanak.mealient.data.network.MealieDataSourceWrapper
import gq.kirmanak.mealient.data.share.ParseRecipeDataSource
import gq.kirmanak.mealient.data.share.ShareRecipeRepo
import gq.kirmanak.mealient.data.share.ShareRecipeRepoImpl

@Module
@InstallIn(SingletonComponent::class)
interface ShareRecipeModule {

    @Binds
    fun bindShareRecipeRepo(shareRecipeRepoImpl: ShareRecipeRepoImpl): ShareRecipeRepo

    @Binds
    fun bindParseRecipeDataSource(mealieDataSourceWrapper: MealieDataSourceWrapper): ParseRecipeDataSource
}