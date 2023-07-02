package gq.kirmanak.mealient.shopping_lists

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gq.kirmanak.mealient.shopping_lists.network.ShoppingListsDataSource
import gq.kirmanak.mealient.shopping_lists.network.ShoppingListsDataSourceImpl
import gq.kirmanak.mealient.shopping_lists.repo.ShoppingListsPagingSourceFactory
import gq.kirmanak.mealient.shopping_lists.repo.ShoppingListsPagingSourceFactoryImpl
import gq.kirmanak.mealient.shopping_lists.repo.ShoppingListsRepo
import gq.kirmanak.mealient.shopping_lists.repo.ShoppingListsRepoImpl
import gq.kirmanak.mealient.shopping_lists.util.LoadingHelperFactory
import gq.kirmanak.mealient.shopping_lists.util.LoadingHelperFactoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ShoppingListsModule {

    @Binds
    @Singleton
    fun bindShoppingListsDataSource(impl: ShoppingListsDataSourceImpl): ShoppingListsDataSource

    @Binds
    @Singleton
    fun bindShoppingListsRepo(impl: ShoppingListsRepoImpl): ShoppingListsRepo

    @Binds
    @Singleton
    fun bindShoppingListsPagingSourceFactory(impl: ShoppingListsPagingSourceFactoryImpl): ShoppingListsPagingSourceFactory

    @Binds
    fun bindLoadingHelperFactory(impl: LoadingHelperFactoryImpl): LoadingHelperFactory
}