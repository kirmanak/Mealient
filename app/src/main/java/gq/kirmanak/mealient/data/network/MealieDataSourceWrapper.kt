package gq.kirmanak.mealient.data.network

import gq.kirmanak.mealient.data.add.AddRecipeDataSource
import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.data.baseurl.BaseURLStorage
import gq.kirmanak.mealient.data.baseurl.VersionDataSource
import gq.kirmanak.mealient.data.baseurl.VersionInfo
import gq.kirmanak.mealient.data.recipes.network.RecipeDataSource
import gq.kirmanak.mealient.datasource.MealieDataSource
import gq.kirmanak.mealient.datasource.models.AddRecipeRequest
import gq.kirmanak.mealient.datasource.models.GetRecipeResponse
import gq.kirmanak.mealient.datasource.models.GetRecipeSummaryResponse
import gq.kirmanak.mealient.datasource.models.NetworkError
import gq.kirmanak.mealient.extensions.toVersionInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MealieDataSourceWrapper @Inject constructor(
    private val baseURLStorage: BaseURLStorage,
    private val authRepo: AuthRepo,
    private val mealieDataSource: MealieDataSource,
) : AddRecipeDataSource, RecipeDataSource, VersionDataSource {

    override suspend fun addRecipe(recipe: AddRecipeRequest): String =
        withAuthHeader { token -> addRecipe(getUrl(), token, recipe) }

    override suspend fun getVersionInfo(baseUrl: String): VersionInfo =
        mealieDataSource.getVersionInfo(baseUrl).toVersionInfo()

    override suspend fun requestRecipes(start: Int, limit: Int): List<GetRecipeSummaryResponse> =
        withAuthHeader { token -> requestRecipes(getUrl(), token, start, limit) }

    override suspend fun requestRecipeInfo(slug: String): GetRecipeResponse =
        withAuthHeader { token -> requestRecipeInfo(getUrl(), token, slug) }

    private suspend fun getUrl() = baseURLStorage.requireBaseURL()

    private suspend inline fun <T> withAuthHeader(block: MealieDataSource.(String?) -> T): T =
        mealieDataSource.runCatching { block(authRepo.getAuthHeader()) }.getOrElse {
            if (it is NetworkError.Unauthorized) {
                authRepo.invalidateAuthHeader()
                // Trying again with new authentication header
                mealieDataSource.block(authRepo.getAuthHeader())
            } else {
                throw it
            }
        }
}