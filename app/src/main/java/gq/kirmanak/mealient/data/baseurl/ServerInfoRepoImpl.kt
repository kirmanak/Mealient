package gq.kirmanak.mealient.data.baseurl

import gq.kirmanak.mealient.datasource.NetworkError
import gq.kirmanak.mealient.logging.Logger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServerInfoRepoImpl @Inject constructor(
    private val serverInfoStorage: ServerInfoStorage,
    private val versionDataSource: VersionDataSource,
    private val logger: Logger,
) : ServerInfoRepo {

    override suspend fun getUrl(): String? {
        val result = serverInfoStorage.getBaseURL()
        logger.v { "getUrl() returned: $result" }
        return result
    }

    override suspend fun requireUrl(): String {
        val result = checkNotNull(getUrl()) { "Server URL was null when it was required" }
        logger.v { "requireUrl() returned: $result" }
        return result
    }

    override suspend fun getVersion(): ServerVersion {
        var version = serverInfoStorage.getServerVersion()
        val serverVersion = if (version == null) {
            logger.d { "getVersion: version is null, requesting" }
            version = versionDataSource.getVersionInfo(requireUrl()).version
            val result = determineServerVersion(version)
            serverInfoStorage.storeServerVersion(version)
            result
        } else {
            determineServerVersion(version)
        }
        logger.v { "getVersion() returned: $serverVersion from $version" }
        return serverVersion
    }

    private fun determineServerVersion(version: String): ServerVersion = when {
        version.startsWith("v0") -> ServerVersion.V0
        version.startsWith("v1") -> ServerVersion.V1
        else -> throw NetworkError.NotMealie(IllegalStateException("Server version is unknown: $version"))
    }

    override suspend fun storeBaseURL(baseURL: String, version: String) {
        logger.v { "storeBaseURL() called with: baseURL = $baseURL, version = $version" }
        serverInfoStorage.storeBaseURL(baseURL, version)
    }
}