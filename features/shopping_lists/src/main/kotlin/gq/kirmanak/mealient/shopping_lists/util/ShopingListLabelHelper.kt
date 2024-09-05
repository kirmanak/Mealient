package gq.kirmanak.mealient.shopping_lists.util

import gq.kirmanak.mealient.datasource.models.GetItemLabelResponse
import gq.kirmanak.mealient.shopping_lists.ui.details.ShoppingListItemState
import gq.kirmanak.mealient.shopping_lists.ui.details.checked

sealed class ItemLabelGroup {
    data object DefaultLabel : ItemLabelGroup()
    data object CheckedItems : ItemLabelGroup()
    data class Label(val label: GetItemLabelResponse) : ItemLabelGroup()
}

/**
 * Function that sorts items by label. The function returns a list of ShoppingListItemStates
 * where items are grouped by label. The list is sorted in the following way:
 * 1. Unchecked items are grouped by label.
 * 2. Items without a label.
 * 3. Checked items regardless of label information.
 *
 * The List contains `ShoppingListItemStates` with each new label group starting with an
 * `ItemLabel` state and followed by the items with that label.
 * The items within a group are sorted by label name.
 */
fun groupItemsByLabel(
    items: List<ShoppingListItemState>
): List<ShoppingListItemState> {

    // Group unchecked items by label and sort each group by label name
    val uncheckedItemsGroupedByLabel = items
        .filterNot { it.checked }
        .groupBy { item ->
            (item as? ShoppingListItemState.ExistingItem)?.item?.label?.let {
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

    /**
     * Helper function to add a group of items with a label to a list.
     */
    fun addLabeledGroupToList(
        result: MutableList<ShoppingListItemState>,
        items: List<ShoppingListItemState>,
        label: ItemLabelGroup?
    ) {
        if (label != null) {
            result.add(
                ShoppingListItemState.ItemLabel(
                    group = label,
                )
            )
        }
        result.addAll(items)
    }

    // Add groups to the result list in the correct order
    val result = mutableListOf<ShoppingListItemState>()

    uncheckedItemsGroupedByLabel.forEach { (labelGroup, items) ->
        addLabeledGroupToList(result, items, labelGroup)
    }
    if (uncheckedItemsNoLabel.isNotEmpty()) {
        // Only add DefaultLabel if there are items with a label to avoid cluttering the UI
        if (result.isNotEmpty()) {
            addLabeledGroupToList(result, uncheckedItemsNoLabel, ItemLabelGroup.DefaultLabel)
        } else {
            addLabeledGroupToList(result, uncheckedItemsNoLabel, null)
        }
    }
    if (checkedItems.isNotEmpty()) {
        addLabeledGroupToList(result, checkedItems, ItemLabelGroup.CheckedItems)
    }

    return result
}