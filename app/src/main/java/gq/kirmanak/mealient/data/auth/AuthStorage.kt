package gq.kirmanak.mealient.data.auth

import kotlinx.coroutines.flow.Flow

interface AuthStorage {

    val authTokenFlow: Flow<String?>

    suspend fun setAuthToken(authToken: String?)

    suspend fun getAuthToken(): String?
}