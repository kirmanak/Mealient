package gq.kirmanak.mealient.data.auth

import kotlinx.coroutines.flow.Flow

interface AuthStorage {

    val authHeaderFlow: Flow<String?>

    suspend fun setAuthHeader(authHeader: String?)

    suspend fun getAuthHeader(): String?

    suspend fun setEmail(email: String?)

    suspend fun getEmail(): String?

    suspend fun setPassword(password: String?)

    suspend fun getPassword(): String?
}