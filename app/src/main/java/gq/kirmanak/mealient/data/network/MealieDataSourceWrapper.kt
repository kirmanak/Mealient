package gq.kirmanak.mealient.data.network

import gq.kirmanak.mealient.data.add.AddRecipeDataSource
import gq.kirmanak.mealient.data.baseurl.ServerInfoRepo
import gq.kirmanak.mealient.data.baseurl.ServerVersion
import gq.kirmanak.mealient.data.recipes.network.RecipeDataSource
import gq.kirmanak.mealient.data.share.ParseRecipeDataSource
import gq.kirmanak.mealient.datasource.models.AddRecipeInfo
import gq.kirmanak.mealient.datasource.models.FullRecipeInfo
import gq.kirmanak.mealient.datasource.models.ParseRecipeURLInfo
import gq.kirmanak.mealient.datasource.models.RecipeSummaryInfo
import gq.kirmanak.mealient.datasource.v0.MealieDataSourceV0
import gq.kirmanak.mealient.datasource.v0.toFullRecipeInfo
import gq.kirmanak.mealient.datasource.v0.toRecipeSummaryInfo
import gq.kirmanak.mealient.datasource.v0.toV0Request
import gq.kirmanak.mealient.datasource.v1.MealieDataSourceV1
import gq.kirmanak.mealient.datasource.v1.toFullRecipeInfo
import gq.kirmanak.mealient.datasource.v1.toRecipeSummaryInfo
import gq.kirmanak.mealient.datasource.v1.toV1CreateRequest
import gq.kirmanak.mealient.datasource.v1.toV1Request
import gq.kirmanak.mealient.datasource.v1.toV1UpdateRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MealieDataSourceWrapper @Inject constructor(
    private val serverInfoRepo: ServerInfoRepo,
    private val v0Source: MealieDataSourceV0,
    private val v1Source: MealieDataSourceV1,
) : AddRecipeDataSource, RecipeDataSource, ParseRecipeDataSource {

    private suspend fun getVersion(): ServerVersion = serverInfoRepo.getVersion()

    override suspend fun addRecipe(recipe: AddRecipeInfo): String = when (getVersion()) {
        ServerVersion.V0 -> v0Source.addRecipe(recipe.toV0Request())
        ServerVersion.V1 -> {
            val slug = v1Source.createRecipe(recipe.toV1CreateRequest())
            v1Source.updateRecipe(slug, recipe.toV1UpdateRequest())
            slug
        }
    }

    override suspend fun requestRecipes(
        start: Int,
        limit: Int,
    ): List<RecipeSummaryInfo> = when (getVersion()) {
        ServerVersion.V0 -> {
            v0Source.requestRecipes(start, limit).map { it.toRecipeSummaryInfo() }
        }
        ServerVersion.V1 -> {
            // Imagine start is 30 and limit is 15. It means that we already have page 1 and 2, now we need page 3
            val page = start / limit + 1
            v1Source.requestRecipes(page, limit).map { it.toRecipeSummaryInfo() }
        }
    }

    override suspend fun requestRecipeInfo(slug: String): FullRecipeInfo = when (getVersion()) {
        ServerVersion.V0 -> v0Source.requestRecipeInfo(slug).toFullRecipeInfo()
        ServerVersion.V1 -> v1Source.requestRecipeInfo(slug).toFullRecipeInfo()
    }

    override suspend fun parseRecipeFromURL(
        parseRecipeURLInfo: ParseRecipeURLInfo,
    ): String = when (getVersion()) {
        ServerVersion.V0 -> v0Source.parseRecipeFromURL(parseRecipeURLInfo.toV0Request())
        ServerVersion.V1 -> v1Source.parseRecipeFromURL(parseRecipeURLInfo.toV1Request())
    }

    override suspend fun getFavoriteRecipes(): List<String> = when (getVersion()) {
        ServerVersion.V0 -> v0Source.requestUserInfo().favoriteRecipes
        ServerVersion.V1 -> v1Source.requestUserInfo().favoriteRecipes
    }

    override suspend fun updateIsRecipeFavorite(
        recipeSlug: String,
        isFavorite: Boolean
    ) = when (getVersion()) {
        ServerVersion.V0 -> {
            val userId = v0Source.requestUserInfo().id
            if (isFavorite) {
                v0Source.addFavoriteRecipe(userId, recipeSlug)
            } else {
                v0Source.removeFavoriteRecipe(userId, recipeSlug)
            }
        }
        ServerVersion.V1 -> {
            val userId = v1Source.requestUserInfo().id
            if (isFavorite) {
                v1Source.addFavoriteRecipe(userId, recipeSlug)
            } else {
                v1Source.removeFavoriteRecipe(userId, recipeSlug)
            }
        }
    }

    override suspend fun deleteRecipe(recipeSlug: String) = when (getVersion()) {
        ServerVersion.V0 -> v0Source.deleteRecipe(recipeSlug)
        ServerVersion.V1 -> v1Source.deleteRecipe(recipeSlug)
    }
}