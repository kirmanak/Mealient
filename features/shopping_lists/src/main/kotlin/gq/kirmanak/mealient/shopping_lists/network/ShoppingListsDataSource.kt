package gq.kirmanak.mealient.shopping_lists.network

interface ShoppingListsDataSource {

    suspend fun getPage(page: Int, perPage: Int): ShoppingListsInfo
}