package gq.kirmanak.mealient.shopping_lists.repo

import androidx.paging.Pager
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListEntity
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListWithItems

interface ShoppingListsRepo {

    fun createPager(): Pager<Int, ShoppingListEntity>

    suspend fun requestShoppingList(id: String): ShoppingListWithItems
}