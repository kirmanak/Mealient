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
import gq.kirmanak.mealient.datasource.models.NetworkError
import gq.kirmanak.mealient.datasource.v1.MealieDataSourceV1
import gq.kirmanak.mealient.datasource.v1.models.GetRecipeSummaryResponseV1
import gq.kirmanak.mealient.extensions.runCatchingExceptCancel
import gq.kirmanak.mealient.extensions.toVersionInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MealieDataSourceWrapper @Inject constructor(
    private val baseURLStorage: BaseURLStorage,
    private val authRepo: AuthRepo,
    private val mealieDataSource: MealieDataSource,
    private val mealieDataSourceV1: MealieDataSourceV1,
) : AddRecipeDataSource, RecipeDataSource, VersionDataSource {

    override suspend fun addRecipe(recipe: AddRecipeRequest): String =
        withAuthHeader { token -> mealieDataSource.addRecipe(getUrl(), token, recipe) }

    override suspend fun getVersionInfo(baseUrl: String): VersionInfo =
        mealieDataSource.getVersionInfo(baseUrl).toVersionInfo()

    override suspend fun requestRecipes(start: Int, limit: Int): List<GetRecipeSummaryResponseV1> =
        withAuthHeader { token ->
            runCatchingExceptCancel {
                mealieDataSource.requestRecipes(getUrl(), token, start, limit).map {
                    GetRecipeSummaryResponseV1(
                        remoteId = it.remoteId.toString(),
                        name = it.name,
                        slug = it.slug,
                        image = it.image,
                        description = it.description,
                        recipeCategories = it.recipeCategories,
                        tags = it.tags,
                        rating = it.rating,
                        dateAdded = it.dateAdded,
                        dateUpdated = it.dateUpdated,
                    )
                }
            }.getOrElse {
                if (it is NetworkError.NotMealie) {
                    mealieDataSourceV1.requestRecipes(getUrl(), token, start, limit)
                } else {
                    throw it
                }
            }
        }

    override suspend fun requestRecipeInfo(slug: String): GetRecipeResponse =
        withAuthHeader { token -> mealieDataSource.requestRecipeInfo(getUrl(), token, slug) }

    private suspend fun getUrl() = baseURLStorage.requireBaseURL()

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