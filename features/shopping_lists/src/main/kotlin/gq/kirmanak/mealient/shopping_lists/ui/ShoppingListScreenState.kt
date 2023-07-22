package gq.kirmanak.mealient.shopping_lists.ui

import gq.kirmanak.mealient.datasource.models.FoodInfo
import gq.kirmanak.mealient.datasource.models.ShoppingListItemInfo
import gq.kirmanak.mealient.datasource.models.UnitInfo
import java.util.UUID

internal data class ShoppingListScreenState(
    val name: String,
    val listId: String,
    val items: List<ShoppingListItemState>,
    val foods: List<FoodInfo>,
    val units: List<UnitInfo>,
)

sealed class ShoppingListItemState {

    data class ExistingItem(
        val item: ShoppingListItemInfo,
        val isEditing: Boolean = false,
    ) : ShoppingListItemState()

    data class NewItem(
        val item: ShoppingListItemEditorState,
        val id: String = UUID.randomUUID().toString(),
    ) : ShoppingListItemState()
}

val ShoppingListItemState.id: String
    get() = when (this) {
        is ShoppingListItemState.ExistingItem -> item.id
        is ShoppingListItemState.NewItem -> id
    }

val ShoppingListItemState.checked: Boolean
    get() = when (this) {
        is ShoppingListItemState.ExistingItem -> item.checked
        is ShoppingListItemState.NewItem -> false
    }

val ShoppingListItemState.position: Int
    get() = when (this) {
        is ShoppingListItemState.ExistingItem -> item.position
        is ShoppingListItemState.NewItem -> item.position
    }
