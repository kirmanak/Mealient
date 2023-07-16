package gq.kirmanak.mealient.shopping_lists.ui

import gq.kirmanak.mealient.datasource.models.ShoppingListItemInfo

internal data class ShoppingListScreenState(
    val name: String,
    val items: List<ShoppingListItemState>,
)

data class ShoppingListItemState(
    val item: ShoppingListItemInfo,
    val isEditing: Boolean = false,
)
