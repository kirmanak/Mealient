package gq.kirmanak.mealient.shopping_lists.repo

import gq.kirmanak.mealient.datasource.models.CreateShoppingListItemRequest
import gq.kirmanak.mealient.datasource.models.CreateShoppingListRequest
import gq.kirmanak.mealient.datasource.models.GetFoodResponse
import gq.kirmanak.mealient.datasource.models.GetShoppingListItemResponse
import gq.kirmanak.mealient.datasource.models.GetShoppingListResponse
import gq.kirmanak.mealient.datasource.models.GetShoppingListsSummaryResponse
import gq.kirmanak.mealient.datasource.models.GetUnitResponse

interface ShoppingListsRepo {

    suspend fun getShoppingLists(): List<GetShoppingListsSummaryResponse>

    suspend fun getShoppingList(id: String): GetShoppingListResponse

    suspend fun deleteShoppingListItem(id: String)

    suspend fun updateShoppingListItem(item: GetShoppingListItemResponse)

    suspend fun getFoods(): List<GetFoodResponse>

    suspend fun getUnits(): List<GetUnitResponse>

    suspend fun addShoppingListItem(item: CreateShoppingListItemRequest)

    suspend fun addShoppingList(request: CreateShoppingListRequest)
}