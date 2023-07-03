package gq.kirmanak.mealient.datasource.models

data class ShoppingListsInfo(
    val page: Int,
    val perPage: Int,
    val totalPages: Int,
    val totalItems: Int,
    val items: List<ShoppingListInfo>,
)

data class ShoppingListInfo(
    val name: String,
    val id: String,
)