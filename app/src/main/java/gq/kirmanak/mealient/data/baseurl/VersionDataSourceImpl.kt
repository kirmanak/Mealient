package gq.kirmanak.mealient.data.baseurl

import gq.kirmanak.mealient.datasource.MealieDataSource
import gq.kirmanak.mealient.datasource.models.VersionInfo
import gq.kirmanak.mealient.model_mapper.ModelMapper
import javax.inject.Inject

class VersionDataSourceImpl @Inject constructor(
    private val dataSource: MealieDataSource,
    private val modelMapper: ModelMapper,
) : VersionDataSource {

    override suspend fun getVersionInfo(): VersionInfo {
        return modelMapper.toVersionInfo(dataSource.getVersionInfo())
    }
}