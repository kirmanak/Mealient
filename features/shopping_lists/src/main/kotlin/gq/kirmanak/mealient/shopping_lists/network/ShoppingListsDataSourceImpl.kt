package gq.kirmanak.mealient.shopping_lists.network

import gq.kirmanak.mealient.datasource.v1.MealieDataSourceV1
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShoppingListsDataSourceImpl @Inject constructor(
    private val v1Source: MealieDataSourceV1
) : ShoppingListsDataSource {

    override suspend fun getAll(): List<ShoppingListInfo> {
        return v1Source.getShoppingLists().map { it.toShoppingListInfo() }
    }
}

