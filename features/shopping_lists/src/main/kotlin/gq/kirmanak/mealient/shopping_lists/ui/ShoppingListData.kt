package gq.kirmanak.mealient.shopping_lists.ui

import gq.kirmanak.mealient.datasource.models.GetFoodResponse
import gq.kirmanak.mealient.datasource.models.GetShoppingListResponse
import gq.kirmanak.mealient.datasource.models.GetUnitResponse

data class ShoppingListData(
    val foods: List<GetFoodResponse>,
    val units: List<GetUnitResponse>,
    val shoppingList: GetShoppingListResponse,
)
