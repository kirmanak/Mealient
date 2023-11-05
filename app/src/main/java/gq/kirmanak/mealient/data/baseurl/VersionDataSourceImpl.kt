package gq.kirmanak.mealient.data.baseurl

import gq.kirmanak.mealient.datasource.MealieDataSource
import gq.kirmanak.mealient.datasource.models.VersionResponse
import javax.inject.Inject

class VersionDataSourceImpl @Inject constructor(
    private val dataSource: MealieDataSource,
) : VersionDataSource {

    override suspend fun requestVersion(): VersionResponse {
        return dataSource.getVersionInfo()
    }
}