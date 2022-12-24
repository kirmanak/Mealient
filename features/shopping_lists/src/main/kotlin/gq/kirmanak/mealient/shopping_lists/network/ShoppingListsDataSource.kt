package gq.kirmanak.mealient.shopping_lists.network

import gq.kirmanak.mealient.shopping_lists.models.ShoppingListsInfo

interface ShoppingListsDataSource {

    suspend fun getPage(page: Int, perPage: Int): ShoppingListsInfo
}