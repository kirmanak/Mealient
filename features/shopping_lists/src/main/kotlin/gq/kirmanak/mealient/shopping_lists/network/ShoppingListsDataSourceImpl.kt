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
    private val v1Source: MealieDataSource,
    private val modelMapper: ModelMapper,
) : ShoppingListsDataSource {

    override suspend fun getAllShoppingLists(): List<ShoppingListInfo> {
        val response = v1Source.getShoppingLists(1, -1)
        return response.items.map { modelMapper.toShoppingListInfo(it) }
    }

    override suspend fun getShoppingList(
        id: String
    ): FullShoppingListInfo = modelMapper.toFullShoppingListInfo(v1Source.getShoppingList(id))

    override suspend fun deleteShoppingListItem(
        id: String
    ) = v1Source.deleteShoppingListItem(id)

    override suspend fun updateShoppingListItem(
        item: ShoppingListItemInfo
    ) = v1Source.updateShoppingListItem(item)

    override suspend fun getFoods(): List<FoodInfo> = modelMapper.toFoodInfo(v1Source.getFoods())

    override suspend fun getUnits(): List<UnitInfo> = modelMapper.toUnitInfo(v1Source.getUnits())

    override suspend fun addShoppingListItem(
        item: NewShoppingListItemInfo
    ) = v1Source.addShoppingListItem(modelMapper.toV1CreateRequest(item))
}

