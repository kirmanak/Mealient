package gq.kirmanak.mealient.shopping_lists.ui

import gq.kirmanak.mealient.datasource.models.FullShoppingListInfo
import gq.kirmanak.mealient.datasource.models.ShoppingListItemInfo

internal data class ShoppingListScreenState(
    val shoppingList: FullShoppingListInfo,
    val disabledItems: List<ShoppingListItemInfo>,
    val snackbarState: SnackbarState,
)

internal sealed class SnackbarState {

    object Hidden : SnackbarState()

    data class Visible(val message: String) : SnackbarState()
}
