package gq.kirmanak.mealient.shopping_lists.repo

import gq.kirmanak.mealient.datasource.models.CreateShoppingListItemRequest
import gq.kirmanak.mealient.datasource.models.CreateShoppingListRequest
import gq.kirmanak.mealient.datasource.models.GetFoodResponse
import gq.kirmanak.mealient.datasource.models.GetShoppingListItemResponse
import gq.kirmanak.mealient.datasource.models.GetShoppingListResponse
import gq.kirmanak.mealient.datasource.models.GetShoppingListsSummaryResponse
import gq.kirmanak.mealient.datasource.models.GetUnitResponse
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.shopping_lists.network.ShoppingListsDataSource
import javax.inject.Inject

class ShoppingListsRepoImpl @Inject constructor(
    private val dataSource: ShoppingListsDataSource,
    private val logger: Logger,
) : ShoppingListsRepo {

    override suspend fun getShoppingLists(): List<GetShoppingListsSummaryResponse> {
        logger.v { "getShoppingLists() called" }
        return dataSource.getAllShoppingLists()
    }

    override suspend fun getShoppingList(id: String): GetShoppingListResponse {
        logger.v { "getShoppingListItems() called with: id = $id" }
        return dataSource.getShoppingList(id)
    }

    override suspend fun deleteShoppingListItem(id: String) {
        logger.v { "deleteShoppingListItem() called with: id = $id" }
        dataSource.deleteShoppingListItem(id)
    }

    override suspend fun updateShoppingListItem(item: GetShoppingListItemResponse) {
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

    override suspend fun addShoppingList(request: CreateShoppingListRequest) {
        logger.v { "addShoppingList() called with: request = $request" }
        dataSource.addShoppingList(request)
    }
}