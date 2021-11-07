package gq.kirmanak.mealie.data.auth

interface AuthRepo {
    suspend fun isAuthenticated(): Boolean

    suspend fun authenticate(username: String, password: String, baseUrl: String): Throwable?

    suspend fun getBaseUrl(): String?

    suspend fun getToken(): String?
}