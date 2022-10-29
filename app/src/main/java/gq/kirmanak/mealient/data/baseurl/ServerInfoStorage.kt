package gq.kirmanak.mealient.data.baseurl

interface ServerInfoStorage {

    suspend fun getBaseURL(): String?

    suspend fun storeBaseURL(baseURL: String, version: String)

    suspend fun storeServerVersion(version: String)

    suspend fun getServerVersion(): String?
}