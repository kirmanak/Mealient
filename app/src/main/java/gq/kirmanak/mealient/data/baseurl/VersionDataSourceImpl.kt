package gq.kirmanak.mealient.data.baseurl

import gq.kirmanak.mealient.data.network.NetworkError
import gq.kirmanak.mealient.data.network.ServiceFactory
import gq.kirmanak.mealient.extensions.versionInfo
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VersionDataSourceImpl @Inject constructor(
    private val serviceFactory: ServiceFactory<VersionService>,
) : VersionDataSource {

    override suspend fun getVersionInfo(baseUrl: String): VersionInfo {
        Timber.v("getVersionInfo() called with: baseUrl = $baseUrl")

        val service = try {
            serviceFactory.provideService(baseUrl)
        } catch (e: Exception) {
            Timber.e(e, "getVersionInfo: can't create service")
            throw NetworkError.MalformedUrl(e)
        }

        val response = try {
            service.getVersion()
        } catch (e: Exception) {
            Timber.e(e, "getVersionInfo: can't request version")
            when (e) {
                is HttpException, is SerializationException -> throw NetworkError.NotMealie(e)
                else -> throw NetworkError.NoServerConnection(e)
            }
        }

        return response.versionInfo()
    }
}