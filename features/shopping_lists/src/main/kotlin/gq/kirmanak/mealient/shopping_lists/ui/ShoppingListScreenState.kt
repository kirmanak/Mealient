package gq.kirmanak.mealient.shopping_lists.ui

import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListItemWithRecipes
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListWithItems

data class ShoppingListScreenState(
    val shoppingList: ShoppingListWithItems,
    val disabledItems: List<ShoppingListItemWithRecipes>,
)
