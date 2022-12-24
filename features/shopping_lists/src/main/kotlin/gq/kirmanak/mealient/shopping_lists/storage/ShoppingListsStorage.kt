package gq.kirmanak.mealient.shopping_lists.storage

import androidx.paging.PagingSource
import gq.kirmanak.mealient.database.recipe.entity.ShoppingListEntity

interface ShoppingListsStorage {

    suspend fun saveShoppingLists(shoppingLists: List<ShoppingListEntity>)

    suspend fun refreshShoppingLists(shoppingLists: List<ShoppingListEntity>)

    fun queryShoppingLists(): PagingSource<Int, ShoppingListEntity>
}