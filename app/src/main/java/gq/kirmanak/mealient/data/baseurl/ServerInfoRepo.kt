package gq.kirmanak.mealient.data.baseurl

import gq.kirmanak.mealient.datasource.models.VersionResponse
import kotlinx.coroutines.flow.Flow

interface ServerInfoRepo {

    val baseUrlFlow: Flow<String?>

    suspend fun getUrl(): String?

    suspend fun tryBaseURL(baseURL: String): Result<VersionResponse>

}

