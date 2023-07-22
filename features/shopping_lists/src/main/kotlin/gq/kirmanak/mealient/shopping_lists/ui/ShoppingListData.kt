package gq.kirmanak.mealient.shopping_lists.ui

import gq.kirmanak.mealient.datasource.models.FoodInfo
import gq.kirmanak.mealient.datasource.models.FullShoppingListInfo
import gq.kirmanak.mealient.datasource.models.UnitInfo

data class ShoppingListData(
    val foods: List<FoodInfo>,
    val units: List<UnitInfo>,
    val shoppingList: FullShoppingListInfo,
)
