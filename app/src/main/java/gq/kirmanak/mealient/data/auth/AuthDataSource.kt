package gq.kirmanak.mealient.data.auth

interface AuthDataSource {
    /**
     * Tries to acquire authentication token using the provided credentials
     */
    suspend fun authenticate(username: String, password: String): String

    suspend fun createApiToken(loginToken: String, name: String): String
}