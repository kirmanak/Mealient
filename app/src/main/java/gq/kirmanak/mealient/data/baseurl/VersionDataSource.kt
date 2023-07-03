package gq.kirmanak.mealient.data.baseurl

import gq.kirmanak.mealient.datasource.models.VersionInfo

interface VersionDataSource {

    suspend fun getVersionInfo(): VersionInfo
}