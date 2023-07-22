package gq.kirmanak.mealient.shopping_lists.ui

import gq.kirmanak.mealient.datasource.models.ShoppingListItemInfo

data class ShoppingListEditingState(
    val deletedItemIds: Set<String> = emptySet(),
    val editingItemIds: Set<String> = emptySet(),
    val modifiedItems: Map<String, ShoppingListItemInfo> = emptyMap(),
    val newItems: List<ShoppingListEditorState> = emptyList(),
)
