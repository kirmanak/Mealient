package gq.kirmanak.mealient.shopping_lists.network

import gq.kirmanak.mealient.datasource.MealieDataSource
import gq.kirmanak.mealient.datasource.models.FoodInfo
import gq.kirmanak.mealient.datasource.models.FullShoppingListInfo
import gq.kirmanak.mealient.datasource.models.NewShoppingListItemInfo
import gq.kirmanak.mealient.datasource.models.ShoppingListInfo
import gq.kirmanak.mealient.datasource.models.ShoppingListItemInfo
import gq.kirmanak.mealient.datasource.models.UnitInfo
import gq.kirmanak.mealient.model_mapper.ModelMapper
import javax.inject.Inject

class ShoppingListsDataSourceImpl @Inject constructor(
    private val dataSource: MealieDataSource,
    private val modelMapper: ModelMapper,
) : ShoppingListsDataSource {

    override suspend fun getAllShoppingLists(): List<ShoppingListInfo> {
        val response = dataSource.getShoppingLists(1, -1)
        return response.items.map { modelMapper.toShoppingListInfo(it) }
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

    override suspend fun getFoods(): List<FoodInfo> = modelMapper.toFoodInfo(dataSource.getFoods())

    override suspend fun getUnits(): List<UnitInfo> = modelMapper.toUnitInfo(dataSource.getUnits())

    override suspend fun addShoppingListItem(
        item: NewShoppingListItemInfo
    ) = dataSource.addShoppingListItem(modelMapper.toCreateRequest(item))
}

