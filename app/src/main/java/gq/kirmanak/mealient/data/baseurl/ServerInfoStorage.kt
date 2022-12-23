package gq.kirmanak.mealient.data.baseurl

import kotlinx.coroutines.flow.Flow

interface ServerInfoStorage {

    suspend fun getBaseURL(): String?

    suspend fun storeBaseURL(baseURL: String)

    suspend fun storeBaseURL(baseURL: String?, version: String?)

    suspend fun storeServerVersion(version: String)

    suspend fun getServerVersion(): String?

    fun serverVersionUpdates(): Flow<String?>
}