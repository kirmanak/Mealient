package gq.kirmanak.mealient.shopping_lists.network

import gq.kirmanak.mealient.datasource.models.FullShoppingListInfo
import gq.kirmanak.mealient.datasource.models.ShoppingListInfo
import gq.kirmanak.mealient.datasource.models.ShoppingListsInfo
import gq.kirmanak.mealient.datasource.v1.MealieDataSourceV1
import gq.kirmanak.mealient.model_mapper.ModelMapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShoppingListsDataSourceImpl @Inject constructor(
    private val v1Source: MealieDataSourceV1,
    private val modelMapper: ModelMapper,
) : ShoppingListsDataSource {

    override suspend fun getPage(
        page: Int,
        perPage: Int
    ): ShoppingListsInfo = modelMapper.toShoppingListsInfo(v1Source.getShoppingLists(page, perPage))

    override suspend fun getAllShoppingLists(): List<ShoppingListInfo> {
        val response = v1Source.getShoppingLists(1, -1)
        return response.items.map { modelMapper.toShoppingListInfo(it) }
    }

    override suspend fun getShoppingList(
        id: String
    ): FullShoppingListInfo = modelMapper.toFullShoppingListInfo(v1Source.getShoppingList(id))

    override suspend fun updateIsShoppingListItemChecked(
        id: String,
        checked: Boolean,
    ) = v1Source.updateIsShoppingListItemChecked(id, checked)
}

