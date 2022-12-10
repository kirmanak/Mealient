package gq.kirmanak.mealient.datasource

interface AuthenticationProvider {

    suspend fun getAuthHeader(): String?

    suspend fun invalidateAuthHeader()
}