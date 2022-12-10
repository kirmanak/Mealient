package gq.kirmanak.mealient.data.network

import gq.kirmanak.mealient.data.add.AddRecipeDataSource
import gq.kirmanak.mealient.data.add.AddRecipeInfo
import gq.kirmanak.mealient.data.baseurl.ServerInfoRepo
import gq.kirmanak.mealient.data.baseurl.ServerVersion
import gq.kirmanak.mealient.data.recipes.network.FullRecipeInfo
import gq.kirmanak.mealient.data.recipes.network.RecipeDataSource
import gq.kirmanak.mealient.data.recipes.network.RecipeSummaryInfo
import gq.kirmanak.mealient.data.share.ParseRecipeDataSource
import gq.kirmanak.mealient.data.share.ParseRecipeURLInfo
import gq.kirmanak.mealient.datasource.v0.MealieDataSourceV0
import gq.kirmanak.mealient.datasource.v1.MealieDataSourceV1
import gq.kirmanak.mealient.extensions.toFullRecipeInfo
import gq.kirmanak.mealient.extensions.toRecipeSummaryInfo
import gq.kirmanak.mealient.extensions.toV0Request
import gq.kirmanak.mealient.extensions.toV1CreateRequest
import gq.kirmanak.mealient.extensions.toV1Request
import gq.kirmanak.mealient.extensions.toV1UpdateRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MealieDataSourceWrapper @Inject constructor(
    private val serverInfoRepo: ServerInfoRepo,
    private val v0Source: MealieDataSourceV0,
    private val v1Source: MealieDataSourceV1,
) : AddRecipeDataSource, RecipeDataSource, ParseRecipeDataSource {

    override suspend fun addRecipe(
        recipe: AddRecipeInfo,
    ): String = makeCall { url, version ->
        when (version) {
            ServerVersion.V0 -> v0Source.addRecipe(url, recipe.toV0Request())
            ServerVersion.V1 -> {
                val slug = v1Source.createRecipe(url, recipe.toV1CreateRequest())
                v1Source.updateRecipe(url, slug, recipe.toV1UpdateRequest())
                slug
            }
        }
    }

    override suspend fun requestRecipes(
        start: Int,
        limit: Int,
    ): List<RecipeSummaryInfo> = makeCall { url, version ->
        when (version) {
            ServerVersion.V0 -> {
                v0Source.requestRecipes(url, start, limit).map { it.toRecipeSummaryInfo() }
            }
            ServerVersion.V1 -> {
                // Imagine start is 30 and limit is 15. It means that we already have page 1 and 2, now we need page 3
                val page = start / limit + 1
                v1Source.requestRecipes(url, page, limit).map { it.toRecipeSummaryInfo() }
            }
        }
    }

    override suspend fun requestRecipeInfo(
        slug: String,
    ): FullRecipeInfo = makeCall { url, version ->
        when (version) {
            ServerVersion.V0 -> v0Source.requestRecipeInfo(url, slug).toFullRecipeInfo()
            ServerVersion.V1 -> v1Source.requestRecipeInfo(url, slug).toFullRecipeInfo()
        }
    }

    override suspend fun parseRecipeFromURL(
        parseRecipeURLInfo: ParseRecipeURLInfo,
    ): String = makeCall { url, version ->
        when (version) {
            ServerVersion.V0 -> {
                v0Source.parseRecipeFromURL(url, parseRecipeURLInfo.toV0Request())
            }
            ServerVersion.V1 -> {
                v1Source.parseRecipeFromURL(url, parseRecipeURLInfo.toV1Request())
            }
        }
    }

    private suspend inline fun <T> makeCall(block: (String, ServerVersion) -> T): T {
        val url = serverInfoRepo.requireUrl()
        val version = serverInfoRepo.getVersion()
        return block(url, version)
    }
}