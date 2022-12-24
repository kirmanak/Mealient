package gq.kirmanak.mealient.shopping_lists.storage

import androidx.paging.PagingSource
import gq.kirmanak.mealient.database.recipe.ShoppingListsDao
import gq.kirmanak.mealient.database.recipe.entity.ShoppingListEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShoppingListsStorageImpl @Inject constructor(
    private val shoppingListsDao: ShoppingListsDao
) : ShoppingListsStorage {

    override suspend fun saveShoppingLists(shoppingLists: List<ShoppingListEntity>) {
        shoppingListsDao.insertShoppingLists(shoppingLists)
    }

    override fun queryShoppingLists(): PagingSource<Int, ShoppingListEntity> {
        return shoppingListsDao.queryShoppingListsByPages()
    }
}