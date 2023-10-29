package gq.kirmanak.mealient.datasource

interface AuthenticationProvider {

    suspend fun getAuthHeader(): String?

    suspend fun getAuthToken(): String?

    suspend fun logout()

}