package gq.kirmanak.mealient.database.shopping_lists

import androidx.paging.PagingSource
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListEntity

interface ShoppingListsStorage {

    suspend fun saveShoppingLists(shoppingLists: List<ShoppingListEntity>)

    suspend fun refreshShoppingLists(shoppingLists: List<ShoppingListEntity>)

    fun queryShoppingLists(): PagingSource<Int, ShoppingListEntity>
}