package gq.kirmanak.mealient.data.baseurl

interface ServerInfoStorage {

    suspend fun getBaseURL(): String?

    suspend fun storeBaseURL(baseURL: String?)

}