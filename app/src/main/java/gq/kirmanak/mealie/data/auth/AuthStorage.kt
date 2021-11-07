package gq.kirmanak.mealie.data.auth

interface AuthStorage {
    suspend fun isAuthenticated(): Boolean
    suspend fun storeToken(token: String)
}