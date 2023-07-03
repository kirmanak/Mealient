package gq.kirmanak.mealient.shopping_lists.repo

import gq.kirmanak.mealient.datasource.models.FullShoppingListInfo
import gq.kirmanak.mealient.datasource.models.ShoppingListInfo
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.shopping_lists.network.ShoppingListsDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShoppingListsRepoImpl @Inject constructor(
    private val dataSource: ShoppingListsDataSource,
    private val logger: Logger,
) : ShoppingListsRepo {

    override suspend fun updateIsShoppingListItemChecked(id: String, isChecked: Boolean) {
        logger.v { "updateIsShoppingListItemChecked() called with: id = $id, isChecked = $isChecked" }
        dataSource.updateIsShoppingListItemChecked(id, isChecked)
    }

    override suspend fun getShoppingLists(): List<ShoppingListInfo> {
        logger.v { "getShoppingLists() called" }
        return dataSource.getAllShoppingLists()
    }

    override suspend fun getShoppingList(id: String): FullShoppingListInfo {
        logger.v { "getShoppingListItems() called with: id = $id" }
        return dataSource.getShoppingList(id)
    }
}