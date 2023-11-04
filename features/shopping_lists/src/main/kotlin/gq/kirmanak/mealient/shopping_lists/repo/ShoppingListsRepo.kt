package gq.kirmanak.mealient.shopping_lists.repo

import gq.kirmanak.mealient.datasource.models.CreateShoppingListItemRequest
import gq.kirmanak.mealient.datasource.models.FullShoppingListInfo
import gq.kirmanak.mealient.datasource.models.GetFoodResponse
import gq.kirmanak.mealient.datasource.models.GetShoppingListsSummaryResponse
import gq.kirmanak.mealient.datasource.models.GetUnitResponse
import gq.kirmanak.mealient.datasource.models.ShoppingListItemInfo

interface ShoppingListsRepo {

    suspend fun getShoppingLists(): List<GetShoppingListsSummaryResponse>

    suspend fun getShoppingList(id: String): FullShoppingListInfo

    suspend fun deleteShoppingListItem(id: String)

    suspend fun updateShoppingListItem(item: ShoppingListItemInfo)

    suspend fun getFoods(): List<GetFoodResponse>

    suspend fun getUnits(): List<GetUnitResponse>

    suspend fun addShoppingListItem(item: CreateShoppingListItemRequest)
}