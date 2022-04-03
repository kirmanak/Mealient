package gq.kirmanak.mealient.data.auth

import kotlinx.coroutines.flow.Flow

interface AuthStorage {
    suspend fun storeAuthData(authHeader: String, baseUrl: String)

    suspend fun getBaseUrl(): String?

    suspend fun getAuthHeader(): String?

    fun authHeaderObservable(): Flow<String?>

    suspend fun clearAuthData()
}