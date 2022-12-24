package gq.kirmanak.mealient.shopping_lists.network

import gq.kirmanak.mealient.shopping_lists.models.ShoppingListInfo

data class ShoppingListsInfo(
    val page: Int,
    val perPage: Int,
    val totalPages: Int,
    val totalItems: Int,
    val items: List<ShoppingListInfo>,
)
