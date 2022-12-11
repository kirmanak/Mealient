package gq.kirmanak.mealient.data.baseurl

interface VersionDataSource {

    suspend fun getVersionInfo(): VersionInfo
}