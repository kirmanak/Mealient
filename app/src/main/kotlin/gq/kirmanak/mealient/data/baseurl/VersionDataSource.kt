package gq.kirmanak.mealient.data.baseurl

import gq.kirmanak.mealient.data.network.NetworkError

interface VersionDataSource {

    @Throws(NetworkError::class)
    suspend fun getVersionInfo(baseUrl: String): VersionInfo
}