package gq.kirmanak.mealient.data.network

import gq.kirmanak.mealient.data.add.AddRecipeDataSource
import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.data.baseurl.BaseURLStorage
import gq.kirmanak.mealient.data.baseurl.VersionDataSource
import gq.kirmanak.mealient.data.baseurl.VersionInfo
import gq.kirmanak.mealient.data.recipes.network.FullRecipeInfo
import gq.kirmanak.mealient.data.recipes.network.RecipeDataSource
import gq.kirmanak.mealient.data.recipes.network.RecipeSummaryInfo
import gq.kirmanak.mealient.datasource.MealieDataSource
import gq.kirmanak.mealient.datasource.models.AddRecipeRequest
import gq.kirmanak.mealient.datasource.models.NetworkError
import gq.kirmanak.mealient.datasource.v1.MealieDataSourceV1
import gq.kirmanak.mealient.extensions.runCatchingExceptCancel
import gq.kirmanak.mealient.extensions.toFullRecipeInfo
import gq.kirmanak.mealient.extensions.toRecipeSummaryInfo
import gq.kirmanak.mealient.extensions.toVersionInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MealieDataSourceWrapper @Inject constructor(
    private val baseURLStorage: BaseURLStorage,
    private val authRepo: AuthRepo,
    private val source: MealieDataSource,
    private val v1Source: MealieDataSourceV1,
) : AddRecipeDataSource, RecipeDataSource, VersionDataSource {

    override suspend fun addRecipe(recipe: AddRecipeRequest): String =
        withAuthHeader { token -> source.addRecipe(getUrl(), token, recipe) }

    override suspend fun getVersionInfo(baseUrl: String): VersionInfo =
        runCatchingExceptCancel {
            source.getVersionInfo(baseUrl).toVersionInfo()
        }.getOrElse {
            if (it is NetworkError.NotMealie) {
                v1Source.getVersionInfo(baseUrl).toVersionInfo()
            } else {
                throw it
            }
        }

    override suspend fun requestRecipes(start: Int, limit: Int): List<RecipeSummaryInfo> =
        withAuthHeader { token ->
            val url = getUrl()
            if (isV1()) {
                v1Source.requestRecipes(url, token, start, limit).map { it.toRecipeSummaryInfo() }
            } else {
                source.requestRecipes(url, token, start, limit).map { it.toRecipeSummaryInfo() }
            }
        }

    override suspend fun requestRecipeInfo(slug: String): FullRecipeInfo =
        withAuthHeader { token ->
            val url = getUrl()
            if (isV1()) {
                v1Source.requestRecipeInfo(url, token, slug).toFullRecipeInfo()
            } else {
                source.requestRecipeInfo(url, token, slug).toFullRecipeInfo()
            }
        }

    private suspend fun getUrl() = baseURLStorage.requireBaseURL()

    private suspend fun isV1(): Boolean {
        var version = baseURLStorage.getServerVersion()
        if (version == null) {
            version = getVersionInfo(getUrl()).version
            baseURLStorage.storeServerVersion(version)
        }
        return version.startsWith("v1")
    }

    private suspend inline fun <T> withAuthHeader(block: (String?) -> T): T =
        runCatching { block(authRepo.getAuthHeader()) }.getOrElse {
            if (it is NetworkError.Unauthorized) {
                authRepo.invalidateAuthHeader()
                // Trying again with new authentication header
                block(authRepo.getAuthHeader())
            } else {
                throw it
            }
        }
}