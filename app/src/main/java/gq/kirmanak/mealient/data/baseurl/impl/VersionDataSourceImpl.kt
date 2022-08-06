package gq.kirmanak.mealient.data.baseurl.impl

import gq.kirmanak.mealient.data.baseurl.VersionDataSource
import gq.kirmanak.mealient.data.baseurl.VersionInfo
import gq.kirmanak.mealient.data.network.MealieDataSourceWrapper
import gq.kirmanak.mealient.extensions.logAndMapErrors
import gq.kirmanak.mealient.extensions.versionInfo
import gq.kirmanak.mealient.logging.Logger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VersionDataSourceImpl @Inject constructor(
    private val logger: Logger,
    private val mealieDataSourceWrapper: MealieDataSourceWrapper,
) : VersionDataSource {

    override suspend fun getVersionInfo(baseUrl: String): VersionInfo {
        logger.v { "getVersionInfo() called with: baseUrl = $baseUrl" }

        val response = logger.logAndMapErrors(
            block = { mealieDataSourceWrapper.getVersionInfo(baseUrl) },
            logProvider = { "getVersionInfo: can't request version" }
        )

        return response.versionInfo()
    }
}