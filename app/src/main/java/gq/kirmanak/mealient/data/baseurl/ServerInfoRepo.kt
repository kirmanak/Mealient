package gq.kirmanak.mealient.data.baseurl

import kotlinx.coroutines.flow.Flow

interface ServerInfoRepo {

    suspend fun getUrl(): String?

    suspend fun getVersion(): ServerVersion

    suspend fun tryBaseURL(baseURL: String): Result<Unit>

    fun versionUpdates(): Flow<ServerVersion>

}

