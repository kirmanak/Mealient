package gq.kirmanak.mealient.data.baseurl

interface ServerInfoRepo {

    suspend fun getUrl(): String?

    suspend fun requireUrl(): String

    suspend fun getVersion(): ServerVersion

    suspend fun storeBaseURL(baseURL: String, version: String)
}

