package gq.kirmanak.mealient.shopping_lists.ui

import gq.kirmanak.mealient.datasource.models.FoodInfo
import gq.kirmanak.mealient.datasource.models.ShoppingListItemInfo
import gq.kirmanak.mealient.datasource.models.UnitInfo

internal data class ShoppingListScreenState(
    val name: String,
    val items: List<ShoppingListItemState>,
    val foods: List<FoodInfo>,
    val units: List<UnitInfo>,
    val newItems: List<ShoppingListEditorState>,
)

data class ShoppingListItemState(
    val item: ShoppingListItemInfo,
    val isEditing: Boolean = false,
)
