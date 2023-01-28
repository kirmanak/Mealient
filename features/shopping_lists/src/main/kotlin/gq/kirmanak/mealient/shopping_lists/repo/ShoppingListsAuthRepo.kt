package gq.kirmanak.mealient.shopping_lists.repo

import kotlinx.coroutines.flow.Flow

interface ShoppingListsAuthRepo {

    val isAuthorizedFlow: Flow<Boolean>
}