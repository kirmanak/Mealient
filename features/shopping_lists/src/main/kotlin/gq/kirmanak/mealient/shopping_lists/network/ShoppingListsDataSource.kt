package gq.kirmanak.mealient.shopping_lists.network

import gq.kirmanak.mealient.datasource.models.FullShoppingListInfo
import gq.kirmanak.mealient.datasource.models.ShoppingListsInfo

interface ShoppingListsDataSource {

    suspend fun getPage(page: Int, perPage: Int): ShoppingListsInfo

    suspend fun getShoppingList(id: String): FullShoppingListInfo

    suspend fun updateIsShoppingListItemChecked(id: String, checked: Boolean)
}