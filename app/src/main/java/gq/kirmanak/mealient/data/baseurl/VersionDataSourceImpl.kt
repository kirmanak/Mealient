package gq.kirmanak.mealient.data.baseurl

import gq.kirmanak.mealient.datasource.v0.MealieDataSourceV0
import gq.kirmanak.mealient.datasource.v1.MealieDataSourceV1
import gq.kirmanak.mealient.extensions.runCatchingExceptCancel
import gq.kirmanak.mealient.extensions.toVersionInfo
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VersionDataSourceImpl @Inject constructor(
    private val v0Source: MealieDataSourceV0,
    private val v1Source: MealieDataSourceV1,
) : VersionDataSource {

    override suspend fun getVersionInfo(baseUrl: String): VersionInfo {
        val responses = coroutineScope {
            val v0Deferred = async {
                runCatchingExceptCancel { v0Source.getVersionInfo(baseUrl).toVersionInfo() }
            }
            val v1Deferred = async {
                runCatchingExceptCancel { v1Source.getVersionInfo(baseUrl).toVersionInfo() }
            }
            listOf(v0Deferred, v1Deferred).awaitAll()
        }
        val firstSuccess = responses.firstNotNullOfOrNull { it.getOrNull() }
        if (firstSuccess == null) {
            throw responses.firstNotNullOf { it.exceptionOrNull() }
        } else {
            return firstSuccess
        }
    }

}