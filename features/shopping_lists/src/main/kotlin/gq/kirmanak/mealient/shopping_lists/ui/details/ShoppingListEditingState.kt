package gq.kirmanak.mealient.shopping_lists.ui.details

import gq.kirmanak.mealient.datasource.models.GetShoppingListItemResponse

data class ShoppingListEditingState(
    val deletedItemIds: Set<String> = emptySet(),
    val editingItemIds: Set<String> = emptySet(),
    val modifiedItems: Map<String, GetShoppingListItemResponse> = emptyMap(),
    val newItems: List<ShoppingListItemState.NewItem> = emptyList(),
)
