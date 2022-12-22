package gq.kirmanak.mealient.shopping_lists

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gq.kirmanak.mealient.shopping_lists.network.ShoppingListsDataSource
import gq.kirmanak.mealient.shopping_lists.network.ShoppingListsDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ShoppingListsModule {

    @Binds
    @Singleton
    fun bindShoppingListsDataSource(impl: ShoppingListsDataSourceImpl): ShoppingListsDataSource
}