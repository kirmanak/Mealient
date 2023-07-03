package gq.kirmanak.mealient.shopping_lists.repo

import gq.kirmanak.mealient.datasource.models.FullShoppingListInfo
import gq.kirmanak.mealient.datasource.models.ShoppingListInfo

interface ShoppingListsRepo {

    suspend fun updateIsShoppingListItemChecked(id: String, isChecked: Boolean)

    suspend fun getShoppingLists(): List<ShoppingListInfo>

    suspend fun getShoppingList(id: String): FullShoppingListInfo
}