package gq.kirmanak.mealie.data.auth

interface AuthStorage {
    suspend fun storeAuthData(token: String, baseUrl: String)

    suspend fun getBaseUrl(): String?

    suspend fun getToken(): String?
}