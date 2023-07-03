package gq.kirmanak.mealient.shopping_lists.repo

import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListWithItems
import gq.kirmanak.mealient.datasource.models.FullShoppingListInfo
import gq.kirmanak.mealient.datasource.models.ShoppingListInfo
import kotlinx.coroutines.flow.Flow

interface ShoppingListsRepo {

    fun shoppingListWithItemsFlow(id: String): Flow<ShoppingListWithItems>

    suspend fun updateShoppingList(id: String)

    suspend fun updateIsShoppingListItemChecked(id: String, isChecked: Boolean)

    suspend fun clearLocalData()

    suspend fun getShoppingLists(): List<ShoppingListInfo>

    suspend fun getShoppingList(id: String): FullShoppingListInfo
}