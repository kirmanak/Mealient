package gq.kirmanak.mealient.shopping_lists.storage

import androidx.paging.PagingSource
import androidx.room.withTransaction
import gq.kirmanak.mealient.database.AppDb
import gq.kirmanak.mealient.database.recipe.ShoppingListsDao
import gq.kirmanak.mealient.database.recipe.entity.ShoppingListEntity
import gq.kirmanak.mealient.logging.Logger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShoppingListsStorageImpl @Inject constructor(
    private val shoppingListsDao: ShoppingListsDao,
    private val appDb: AppDb,
    private val logger: Logger,
) : ShoppingListsStorage {

    override suspend fun saveShoppingLists(shoppingLists: List<ShoppingListEntity>) {
        logger.v { "saveShoppingLists() called with: shoppingLists = $shoppingLists " }
        shoppingListsDao.insertShoppingLists(shoppingLists)
    }

    override fun queryShoppingLists(): PagingSource<Int, ShoppingListEntity> {
        logger.v { "queryShoppingLists() called" }
        return shoppingListsDao.queryShoppingListsByPages()
    }

    override suspend fun refreshShoppingLists(shoppingLists: List<ShoppingListEntity>) {
        logger.v { "refreshShoppingLists() called with: shoppingLists = $shoppingLists" }
        appDb.withTransaction {
            shoppingListsDao.deleteAllShoppingLists()
            shoppingListsDao.insertShoppingLists(shoppingLists)
        }
    }
}