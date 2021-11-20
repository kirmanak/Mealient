package gq.kirmanak.mealient.data.auth

import kotlinx.coroutines.flow.Flow

interface AuthStorage {
    fun storeAuthData(token: String, baseUrl: String)

    suspend fun getBaseUrl(): String?

    suspend fun getToken(): String?

    fun tokenObservable(): Flow<String?>

    fun clearAuthData()
}