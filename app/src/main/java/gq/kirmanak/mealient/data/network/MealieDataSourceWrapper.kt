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

    private suspend fun getVersion(): ServerVersion = serverInfoRepo.getVersion()

    private suspend fun getUrl(): String = serverInfoRepo.requireUrl()

    override suspend fun addRecipe(recipe: AddRecipeInfo): String = when (getVersion()) {
        ServerVersion.V0 -> v0Source.addRecipe(getUrl(), recipe.toV0Request())
        ServerVersion.V1 -> {
            val slug = v1Source.createRecipe(getUrl(), recipe.toV1CreateRequest())
            v1Source.updateRecipe(getUrl(), slug, recipe.toV1UpdateRequest())
            slug
        }
    }

    override suspend fun requestRecipes(
        start: Int,
        limit: Int,
    ): List<RecipeSummaryInfo> = when (getVersion()) {
        ServerVersion.V0 -> {
            v0Source.requestRecipes(getUrl(), start, limit).map { it.toRecipeSummaryInfo() }
        }
        ServerVersion.V1 -> {
            // Imagine start is 30 and limit is 15. It means that we already have page 1 and 2, now we need page 3
            val page = start / limit + 1
            v1Source.requestRecipes(getUrl(), page, limit).map { it.toRecipeSummaryInfo() }
        }
    }

    override suspend fun requestRecipeInfo(slug: String): FullRecipeInfo = when (getVersion()) {
        ServerVersion.V0 -> v0Source.requestRecipeInfo(getUrl(), slug).toFullRecipeInfo()
        ServerVersion.V1 -> v1Source.requestRecipeInfo(getUrl(), slug).toFullRecipeInfo()
    }

    override suspend fun parseRecipeFromURL(
        parseRecipeURLInfo: ParseRecipeURLInfo,
    ): String = when (getVersion()) {
        ServerVersion.V0 -> v0Source.parseRecipeFromURL(getUrl(), parseRecipeURLInfo.toV0Request())
        ServerVersion.V1 -> v1Source.parseRecipeFromURL(getUrl(), parseRecipeURLInfo.toV1Request())
    }

}