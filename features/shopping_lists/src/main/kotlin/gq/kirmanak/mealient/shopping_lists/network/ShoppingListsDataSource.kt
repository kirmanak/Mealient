package gq.kirmanak.mealient.shopping_lists.network

interface ShoppingListsDataSource {

    suspend fun getAll(): List<ShoppingListInfo>
}