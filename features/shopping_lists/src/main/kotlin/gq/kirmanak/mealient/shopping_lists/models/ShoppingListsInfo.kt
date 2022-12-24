package gq.kirmanak.mealient.shopping_lists.models

data class ShoppingListsInfo(
    val page: Int,
    val perPage: Int,
    val totalPages: Int,
    val totalItems: Int,
    val items: List<ShoppingListInfo>,
)
