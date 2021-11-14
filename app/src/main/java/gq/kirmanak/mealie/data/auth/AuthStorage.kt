package gq.kirmanak.mealie.data.auth

import kotlinx.coroutines.flow.Flow

interface AuthStorage {
    suspend fun storeAuthData(token: String, baseUrl: String)

    suspend fun getBaseUrl(): String?

    suspend fun getToken(): String?

    fun tokenObservable(): Flow<String?>
}