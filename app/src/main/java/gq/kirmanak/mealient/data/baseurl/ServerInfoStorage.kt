package gq.kirmanak.mealient.data.baseurl

import kotlinx.coroutines.flow.Flow

interface ServerInfoStorage {

    val baseUrlFlow: Flow<String?>

    suspend fun getBaseURL(): String?

    suspend fun storeBaseURL(baseURL: String?)

}