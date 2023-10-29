package gq.kirmanak.mealient.data.baseurl

import gq.kirmanak.mealient.datasource.ServerUrlProvider
import gq.kirmanak.mealient.datasource.runCatchingExceptCancel
import gq.kirmanak.mealient.logging.Logger
import javax.inject.Inject

class ServerInfoRepoImpl @Inject constructor(
    private val serverInfoStorage: ServerInfoStorage,
    private val versionDataSource: VersionDataSource,
    private val logger: Logger,
) : ServerInfoRepo, ServerUrlProvider {

    override suspend fun getUrl(): String? {
        val result = serverInfoStorage.getBaseURL()
        logger.v { "getUrl() returned: $result" }
        return result
    }

    override suspend fun tryBaseURL(baseURL: String): Result<Unit> {
        val oldBaseUrl = serverInfoStorage.getBaseURL()

        return runCatchingExceptCancel {
            serverInfoStorage.storeBaseURL(baseURL)
            versionDataSource.getVersionInfo()
            Unit
        }.onFailure {
            serverInfoStorage.storeBaseURL(oldBaseUrl)
        }
    }

}