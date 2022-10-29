package gq.kirmanak.mealient.data.baseurl

interface BaseURLStorage {

    suspend fun getBaseURL(): String?

    suspend fun requireBaseURL(): String

    suspend fun storeBaseURL(baseURL: String, version: String)

    suspend fun getServerVersion(): String?
}