package gq.kirmanak.mealient.shopping_lists.repo

import androidx.paging.Pager
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListEntity
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListWithItems
import kotlinx.coroutines.flow.Flow

interface ShoppingListsRepo {

    fun createPager(): Pager<Int, ShoppingListEntity>

    fun getShoppingListWithItems(id: String): Flow<ShoppingListWithItems>

    suspend fun updateShoppingList(id: String)
}