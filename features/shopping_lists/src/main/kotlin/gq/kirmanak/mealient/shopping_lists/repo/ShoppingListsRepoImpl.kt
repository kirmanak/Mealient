package gq.kirmanak.mealient.shopping_lists.repo

import gq.kirmanak.mealient.datasource.models.CreateShoppingListItemRequest
import gq.kirmanak.mealient.datasource.models.FullShoppingListInfo
import gq.kirmanak.mealient.datasource.models.GetFoodResponse
import gq.kirmanak.mealient.datasource.models.GetUnitResponse
import gq.kirmanak.mealient.datasource.models.ShoppingListInfo
import gq.kirmanak.mealient.datasource.models.ShoppingListItemInfo
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.shopping_lists.network.ShoppingListsDataSource
import javax.inject.Inject

class ShoppingListsRepoImpl @Inject constructor(
    private val dataSource: ShoppingListsDataSource,
    private val logger: Logger,
) : ShoppingListsRepo {

    override suspend fun getShoppingLists(): List<ShoppingListInfo> {
        logger.v { "getShoppingLists() called" }
        return dataSource.getAllShoppingLists()
    }

    override suspend fun getShoppingList(id: String): FullShoppingListInfo {
        logger.v { "getShoppingListItems() called with: id = $id" }
        return dataSource.getShoppingList(id)
    }

    override suspend fun deleteShoppingListItem(id: String) {
        logger.v { "deleteShoppingListItem() called with: id = $id" }
        dataSource.deleteShoppingListItem(id)
    }

    override suspend fun updateShoppingListItem(item: ShoppingListItemInfo) {
        logger.v { "updateShoppingListItem() called with: item = $item" }
        dataSource.updateShoppingListItem(item)
    }

    override suspend fun getFoods(): List<GetFoodResponse> {
        logger.v { "getFoods() called" }
        return dataSource.getFoods()
    }

    override suspend fun getUnits(): List<GetUnitResponse> {
        logger.v { "getUnits() called" }
        return dataSource.getUnits()
    }

    override suspend fun addShoppingListItem(item: CreateShoppingListItemRequest) {
        logger.v { "addShoppingListItem() called with: item = $item" }
        dataSource.addShoppingListItem(item)
    }
}