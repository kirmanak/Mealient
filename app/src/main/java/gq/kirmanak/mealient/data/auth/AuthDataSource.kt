package gq.kirmanak.mealient.data.auth

import gq.kirmanak.mealient.data.baseurl.ServerVersion

interface AuthDataSource {
    /**
     * Tries to acquire authentication token using the provided credentials
     */
    suspend fun authenticate(
        username: String,
        password: String,
        baseUrl: String,
        serverVersion: ServerVersion,
    ): String
}