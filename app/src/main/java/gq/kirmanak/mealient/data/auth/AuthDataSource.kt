package gq.kirmanak.mealient.data.auth

interface AuthDataSource {
    /**
     * Tries to acquire authentication token using the provided credentials on specified server.
     */
    suspend fun authenticate(username: String, password: String, baseUrl: String): String
}