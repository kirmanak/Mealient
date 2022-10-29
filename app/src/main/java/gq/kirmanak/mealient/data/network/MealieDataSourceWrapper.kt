package gq.kirmanak.mealient.data.network

import gq.kirmanak.mealient.data.add.AddRecipeDataSource
import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.data.baseurl.ServerInfoRepo
import gq.kirmanak.mealient.data.baseurl.ServerVersion
import gq.kirmanak.mealient.data.recipes.network.FullRecipeInfo
import gq.kirmanak.mealient.data.recipes.network.RecipeDataSource
import gq.kirmanak.mealient.data.recipes.network.RecipeSummaryInfo
import gq.kirmanak.mealient.datasource.NetworkError
import gq.kirmanak.mealient.datasource.v0.MealieDataSourceV0
import gq.kirmanak.mealient.datasource.v0.models.AddRecipeRequestV0
import gq.kirmanak.mealient.datasource.v1.MealieDataSourceV1
import gq.kirmanak.mealient.extensions.toFullRecipeInfo
import gq.kirmanak.mealient.extensions.toRecipeSummaryInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MealieDataSourceWrapper @Inject constructor(
    private val serverInfoRepo: ServerInfoRepo,
    private val authRepo: AuthRepo,
    private val v0source: MealieDataSourceV0,
    private val v1Source: MealieDataSourceV1,
) : AddRecipeDataSource, RecipeDataSource {

    override suspend fun addRecipe(recipe: AddRecipeRequestV0): String = withAuthHeader { token ->
        v0source.addRecipe(getUrl(), token, recipe)
    }

    override suspend fun requestRecipes(
        start: Int,
        limit: Int
    ): List<RecipeSummaryInfo> = withAuthHeader { token ->
        val url = getUrl()
        when (getVersion()) {
            ServerVersion.V0 -> {
                v0source.requestRecipes(url, token, start, limit).map { it.toRecipeSummaryInfo() }
            }
            ServerVersion.V1 -> {
                // Imagine start is 30 and limit is 15. It means that we already have page 1 and 2, now we need page 3
                val page = start / limit + 1
                v1Source.requestRecipes(url, token, page, limit).map { it.toRecipeSummaryInfo() }
            }
        }
    }

    override suspend fun requestRecipeInfo(slug: String): FullRecipeInfo = withAuthHeader { token ->
        val url = getUrl()
        when (getVersion()) {
            ServerVersion.V0 -> v0source.requestRecipeInfo(url, token, slug).toFullRecipeInfo()
            ServerVersion.V1 -> v1Source.requestRecipeInfo(url, token, slug).toFullRecipeInfo()
        }
    }

    private suspend fun getUrl() = serverInfoRepo.requireUrl()

    private suspend fun getVersion() = serverInfoRepo.getVersion()

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