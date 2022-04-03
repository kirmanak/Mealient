package gq.kirmanak.mealient.data.auth

import kotlinx.coroutines.flow.Flow

interface AuthRepo {
    suspend fun authenticate(username: String, password: String, baseUrl: String)

    suspend fun getBaseUrl(): String?

    suspend fun requireBaseUrl(): String

    suspend fun getAuthHeader(): String?

    suspend fun requireAuthHeader(): String

    fun authenticationStatuses(): Flow<Boolean>

    suspend fun logout()
}