package gq.kirmanak.mealient.data.baseurl

import gq.kirmanak.mealient.datasource.models.VersionResponse

interface VersionDataSource {

    suspend fun requestVersion(): VersionResponse
}