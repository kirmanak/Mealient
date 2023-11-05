package gq.kirmanak.mealient.data.auth

import gq.kirmanak.mealient.shopping_lists.repo.ShoppingListsAuthRepo
import kotlinx.coroutines.flow.Flow

interface AuthRepo : ShoppingListsAuthRepo {

    override val isAuthorizedFlow: Flow<Boolean>

    suspend fun authenticate(email: String, password: String)

    suspend fun getAuthToken(): String?

    suspend fun logout()
}