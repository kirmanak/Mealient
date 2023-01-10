package gq.kirmanak.mealient.shopping_lists.ui

import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListItemWithRecipes
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListWithItems

internal data class ShoppingListScreenState(
    val shoppingList: ShoppingListWithItems,
    val disabledItems: List<ShoppingListItemWithRecipes>,
    val snackbarState: SnackbarState,
)

internal sealed class SnackbarState {

    object Hidden : SnackbarState()

    data class Visible(val message: String) : SnackbarState()
}
