package gq.kirmanak.mealient.shopping_lists.network

import gq.kirmanak.mealient.datasource.models.FullShoppingListInfo
import gq.kirmanak.mealient.datasource.models.ShoppingListsInfo
import gq.kirmanak.mealient.datasource.v1.MealieDataSourceV1
import gq.kirmanak.mealient.datasource.v1.toFullShoppingListInfo
import gq.kirmanak.mealient.datasource.v1.toShoppingListsInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShoppingListsDataSourceImpl @Inject constructor(
    private val v1Source: MealieDataSourceV1
) : ShoppingListsDataSource {

    override suspend fun getPage(
        page: Int,
        perPage: Int
    ): ShoppingListsInfo = v1Source.getShoppingLists(page, perPage).toShoppingListsInfo()

    override suspend fun getShoppingList(id: String): FullShoppingListInfo {
        return v1Source.getShoppingList(id).toFullShoppingListInfo()
    }
}

