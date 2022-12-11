package gq.kirmanak.mealient.data.baseurl

interface ServerInfoRepo {

    suspend fun getUrl(): String?

    suspend fun getVersion(): ServerVersion

    suspend fun tryBaseURL(baseURL: String): Result<Unit>

}

