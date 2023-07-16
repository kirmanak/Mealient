package gq.kirmanak.mealient.shopping_lists.network

import gq.kirmanak.mealient.datasource.models.FullShoppingListInfo
import gq.kirmanak.mealient.datasource.models.ShoppingListInfo
import gq.kirmanak.mealient.datasource.models.ShoppingListItemInfo

interface ShoppingListsDataSource {

    suspend fun getAllShoppingLists(): List<ShoppingListInfo>

    suspend fun getShoppingList(id: String): FullShoppingListInfo

    suspend fun deleteShoppingListItem(id: String)

    suspend fun updateShoppingListItem(item: ShoppingListItemInfo)
}