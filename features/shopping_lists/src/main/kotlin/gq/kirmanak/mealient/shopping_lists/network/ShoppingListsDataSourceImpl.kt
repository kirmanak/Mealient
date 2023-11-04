package gq.kirmanak.mealient.shopping_lists.network

import gq.kirmanak.mealient.datasource.MealieDataSource
import gq.kirmanak.mealient.datasource.models.CreateShoppingListItemRequest
import gq.kirmanak.mealient.datasource.models.FullShoppingListInfo
import gq.kirmanak.mealient.datasource.models.GetFoodResponse
import gq.kirmanak.mealient.datasource.models.GetShoppingListsSummaryResponse
import gq.kirmanak.mealient.datasource.models.GetUnitResponse
import gq.kirmanak.mealient.datasource.models.ShoppingListItemInfo
import gq.kirmanak.mealient.model_mapper.ModelMapper
import javax.inject.Inject

class ShoppingListsDataSourceImpl @Inject constructor(
    private val dataSource: MealieDataSource,
    private val modelMapper: ModelMapper,
) : ShoppingListsDataSource {

    override suspend fun getAllShoppingLists(): List<GetShoppingListsSummaryResponse> {
        val response = dataSource.getShoppingLists(1, -1)
        return response.items
    }

    override suspend fun getShoppingList(
        id: String
    ): FullShoppingListInfo = modelMapper.toFullShoppingListInfo(dataSource.getShoppingList(id))

    override suspend fun deleteShoppingListItem(
        id: String
    ) = dataSource.deleteShoppingListItem(id)

    override suspend fun updateShoppingListItem(
        item: ShoppingListItemInfo
    ) = dataSource.updateShoppingListItem(item)

    override suspend fun getFoods(): List<GetFoodResponse> = dataSource.getFoods().items

    override suspend fun getUnits(): List<GetUnitResponse> = dataSource.getUnits().items

    override suspend fun addShoppingListItem(
        item: CreateShoppingListItemRequest
    ) = dataSource.addShoppingListItem(item)
}

