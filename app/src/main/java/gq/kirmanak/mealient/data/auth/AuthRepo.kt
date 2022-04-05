package gq.kirmanak.mealient.data.auth

import kotlinx.coroutines.flow.Flow

interface AuthRepo {

    val isAuthorizedFlow: Flow<Boolean>

    suspend fun authenticate(username: String, password: String)

    suspend fun getAuthHeader(): String?

    suspend fun requireAuthHeader(): String

    suspend fun logout()

    fun invalidateAuthHeader(header: String)
}