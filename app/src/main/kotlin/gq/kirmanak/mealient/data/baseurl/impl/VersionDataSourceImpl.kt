package gq.kirmanak.mealient.data.baseurl.impl

import gq.kirmanak.mealient.data.baseurl.VersionDataSource
import gq.kirmanak.mealient.data.baseurl.VersionInfo
import gq.kirmanak.mealient.data.network.ServiceFactory
import gq.kirmanak.mealient.extensions.mapToNetworkError
import gq.kirmanak.mealient.extensions.runCatchingExceptCancel
import gq.kirmanak.mealient.extensions.versionInfo
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VersionDataSourceImpl @Inject constructor(
    private val serviceFactory: ServiceFactory<VersionService>,
) : VersionDataSource {

    override suspend fun getVersionInfo(baseUrl: String): VersionInfo {
        Timber.v("getVersionInfo() called with: baseUrl = $baseUrl")

        val service = serviceFactory.provideService(baseUrl)
        val response = runCatchingExceptCancel {
            service.getVersion()
        }.getOrElse {
            Timber.e(it, "getVersionInfo: can't request version")
            throw it.mapToNetworkError()
        }

        return response.versionInfo()
    }
}