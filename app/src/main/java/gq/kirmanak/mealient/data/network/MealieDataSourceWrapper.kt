package gq.kirmanak.mealient.data.network

import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.data.baseurl.BaseURLStorage
import gq.kirmanak.mealient.datasource.MealieDataSource
import gq.kirmanak.mealient.datasource.models.*
import gq.kirmanak.mealient.datasource.models.NetworkError
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MealieDataSourceWrapper @Inject constructor(
    private val baseURLStorage: BaseURLStorage,
    private val authRepo: AuthRepo,
    private val mealieDataSource: MealieDataSource,
) {

    suspend fun addRecipe(recipe: AddRecipeRequest): String {
        val baseUrl = baseURLStorage.requireBaseURL()
        return withAuthHeader { token -> addRecipe(baseUrl, token, recipe) }
    }

    suspend fun getVersionInfo(baseUrl: String): VersionResponse {
        return mealieDataSource.getVersionInfo(baseUrl)
    }

    suspend fun requestRecipes(
        start: Int = 0,
        limit: Int = 9999,
    ): List<GetRecipeSummaryResponse> {
        val baseUrl = baseURLStorage.requireBaseURL()
        return withAuthHeader { token -> requestRecipes(baseUrl, token, start, limit) }
    }

    suspend fun requestRecipeInfo(slug: String): GetRecipeResponse {
        val baseUrl = baseURLStorage.requireBaseURL()
        return withAuthHeader { token -> requestRecipeInfo(baseUrl, token, slug) }
    }

    private suspend inline fun <T> withAuthHeader(block: MealieDataSource.(String?) -> T): T =
        mealieDataSource.runCatching { block(authRepo.getAuthHeader()) }.getOrElse {
            if (it is NetworkError.Unauthorized) {
                authRepo.invalidateAuthHeader()
                mealieDataSource.block(authRepo.getAuthHeader())
            } else {
                throw it
            }
        }
}