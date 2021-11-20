package gq.kirmanak.mealient.data.auth

import kotlinx.coroutines.flow.Flow

interface AuthRepo {
    suspend fun authenticate(username: String, password: String, baseUrl: String)

    suspend fun getBaseUrl(): String?

    suspend fun getToken(): String?

    fun authenticationStatuses(): Flow<Boolean>

    fun logout()
}