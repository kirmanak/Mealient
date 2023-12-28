package gq.kirmanak.mealient.data.baseurl

import gq.kirmanak.mealient.datasource.ServerUrlProvider
import gq.kirmanak.mealient.datasource.models.VersionResponse
import gq.kirmanak.mealient.logging.Logger
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ServerInfoRepoImpl @Inject constructor(
    private val serverInfoStorage: ServerInfoStorage,
    private val versionDataSource: VersionDataSource,
    private val logger: Logger,
) : ServerInfoRepo, ServerUrlProvider {

    override val baseUrlFlow: Flow<String?>
        get() = serverInfoStorage.baseUrlFlow

    override suspend fun getUrl(): String? {
        val result = serverInfoStorage.getBaseURL()
        logger.v { "getUrl() returned: $result" }
        return result
    }

    override suspend fun tryBaseURL(baseURL: String): Result<VersionResponse> {
        return versionDataSource.runCatching {
            requestVersion(baseURL)
        }.onSuccess {
            serverInfoStorage.storeBaseURL(baseURL)
        }
    }
}