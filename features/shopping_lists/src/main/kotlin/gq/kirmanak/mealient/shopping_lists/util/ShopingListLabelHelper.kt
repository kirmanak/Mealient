package gq.kirmanak.mealient.shopping_lists.util

import gq.kirmanak.mealient.shopping_lists.ui.details.ShoppingListItemState
import gq.kirmanak.mealient.shopping_lists.ui.details.checked

sealed class ItemLabelGroup {
    data object DefaultLabel : ItemLabelGroup()
    data object CheckedItems : ItemLabelGroup()
    data class Label(val name: String) : ItemLabelGroup()
}

fun groupItemsByLabel(
    items: List<ShoppingListItemState>
): Map<ItemLabelGroup, List<ShoppingListItemState>> {

    // Group unchecked items by label and sort each group by label name
    val uncheckedItemsGroupedByLabel = items
        .filterNot { it.checked }
        .groupBy { item ->
            (item as? ShoppingListItemState.ExistingItem)?.item?.label?.name?.let {
                ItemLabelGroup.Label(it)
            } ?: ItemLabelGroup.DefaultLabel
        }.mapValues { (_, groupedItems) ->
            groupedItems.sortedBy { item ->
                (item as? ShoppingListItemState.ExistingItem)?.item?.label?.name ?: ""
            }
        }.toMutableMap()

    // Remove items with no label from grouped items to prevent them from being displayed first.
    // Store these items in a separate list to add them back to the end of the map later.
    val uncheckedItemsNoLabel = uncheckedItemsGroupedByLabel[ItemLabelGroup.DefaultLabel].orEmpty()
    uncheckedItemsGroupedByLabel.remove(ItemLabelGroup.DefaultLabel)

    // Put checked items as a single group to display them without label-specific headers
    val checkedItems = items.filter { it.checked }
        .sortedBy { item ->
            (item as? ShoppingListItemState.ExistingItem)?.item?.label?.name ?: ""
        }

    // Add all groups to a single map in the following order:
    // 1. Unchecked items grouped by label
    // 2. Items with no label (if it contains items)
    // 3. Checked items (if it contains items)
    return uncheckedItemsGroupedByLabel.apply {
        if (uncheckedItemsNoLabel.isNotEmpty()) {
            this[ItemLabelGroup.DefaultLabel] = uncheckedItemsNoLabel
        }
        if (checkedItems.isNotEmpty()) {
            this[ItemLabelGroup.CheckedItems] = checkedItems
        }
    }
}