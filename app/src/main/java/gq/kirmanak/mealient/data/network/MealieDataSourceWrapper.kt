package gq.kirmanak.mealient.data.network

import gq.kirmanak.mealient.data.add.AddRecipeDataSource
import gq.kirmanak.mealient.data.add.AddRecipeInfo
import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.data.baseurl.ServerInfoRepo
import gq.kirmanak.mealient.data.baseurl.ServerVersion
import gq.kirmanak.mealient.data.recipes.network.FullRecipeInfo
import gq.kirmanak.mealient.data.recipes.network.RecipeDataSource
import gq.kirmanak.mealient.data.recipes.network.RecipeSummaryInfo
import gq.kirmanak.mealient.datasource.NetworkError
import gq.kirmanak.mealient.datasource.runCatchingExceptCancel
import gq.kirmanak.mealient.datasource.v0.MealieDataSourceV0
import gq.kirmanak.mealient.datasource.v1.MealieDataSourceV1
import gq.kirmanak.mealient.extensions.toFullRecipeInfo
import gq.kirmanak.mealient.extensions.toRecipeSummaryInfo
import gq.kirmanak.mealient.extensions.toV0Request
import gq.kirmanak.mealient.extensions.toV1Request
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MealieDataSourceWrapper @Inject constructor(
    private val serverInfoRepo: ServerInfoRepo,
    private val authRepo: AuthRepo,
    private val v0Source: MealieDataSourceV0,
    private val v1Source: MealieDataSourceV1,
) : AddRecipeDataSource, RecipeDataSource {

    override suspend fun addRecipe(
        recipe: AddRecipeInfo,
    ): String = makeCall { token, url, version ->
        when (version) {
            ServerVersion.V0 -> v0Source.addRecipe(url, token, recipe.toV0Request())
            ServerVersion.V1 -> v1Source.addRecipe(url, token, recipe.toV1Request())
        }
    }

    override suspend fun requestRecipes(
        start: Int,
        limit: Int,
    ): List<RecipeSummaryInfo> = makeCall { token, url, version ->
        when (version) {
            ServerVersion.V0 -> {
                v0Source.requestRecipes(url, token, start, limit).map { it.toRecipeSummaryInfo() }
            }
            ServerVersion.V1 -> {
                // Imagine start is 30 and limit is 15. It means that we already have page 1 and 2, now we need page 3
                val page = start / limit + 1
                v1Source.requestRecipes(url, token, page, limit).map { it.toRecipeSummaryInfo() }
            }
        }
    }

    override suspend fun requestRecipeInfo(
        slug: String,
    ): FullRecipeInfo = makeCall { token, url, version ->
        when (version) {
            ServerVersion.V0 -> v0Source.requestRecipeInfo(url, token, slug).toFullRecipeInfo()
            ServerVersion.V1 -> v1Source.requestRecipeInfo(url, token, slug).toFullRecipeInfo()
        }
    }

    private suspend inline fun <T> makeCall(block: (String?, String, ServerVersion) -> T): T {
        val authHeader = authRepo.getAuthHeader()
        val url = serverInfoRepo.requireUrl()
        val version = serverInfoRepo.getVersion()
        return runCatchingExceptCancel { block(authHeader, url, version) }.getOrElse {
            if (it is NetworkError.Unauthorized) {
                authRepo.invalidateAuthHeader()
                // Trying again with new authentication header
                val newHeader = authRepo.getAuthHeader()
                if (newHeader == authHeader) throw it else block(newHeader, url, version)
            } else {
                throw it
            }
        }
    }
}