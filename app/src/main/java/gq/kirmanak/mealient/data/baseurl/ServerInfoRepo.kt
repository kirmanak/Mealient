package gq.kirmanak.mealient.data.baseurl

import kotlinx.coroutines.flow.Flow

interface ServerInfoRepo {

    val baseUrlFlow: Flow<String?>

    suspend fun getUrl(): String?

    suspend fun tryBaseURL(baseURL: String): Result<Unit>

}

