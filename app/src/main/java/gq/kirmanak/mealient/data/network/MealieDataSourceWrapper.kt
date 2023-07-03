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
import gq.kirmanak.mealient.datasource.v1.MealieDataSourceV1
import gq.kirmanak.mealient.model_mapper.ModelMapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MealieDataSourceWrapper @Inject constructor(
    private val serverInfoRepo: ServerInfoRepo,
    private val v0Source: MealieDataSourceV0,
    private val v1Source: MealieDataSourceV1,
    private val modelMapper: ModelMapper,
) : AddRecipeDataSource, RecipeDataSource, ParseRecipeDataSource {

    private suspend fun getVersion(): ServerVersion = serverInfoRepo.getVersion()

    override suspend fun addRecipe(recipe: AddRecipeInfo): String = when (getVersion()) {
        ServerVersion.V0 -> v0Source.addRecipe(modelMapper.toV0Request(recipe))
        ServerVersion.V1 -> {
            val slug = v1Source.createRecipe(modelMapper.toV1CreateRequest(recipe))
            v1Source.updateRecipe(slug, modelMapper.toV1UpdateRequest(recipe))
            slug
        }
    }

    override suspend fun requestRecipes(
        start: Int,
        limit: Int,
    ): List<RecipeSummaryInfo> = when (getVersion()) {
        ServerVersion.V0 -> {
            v0Source.requestRecipes(start, limit).map { modelMapper.toRecipeSummaryInfo(it) }
        }
        ServerVersion.V1 -> {
            // Imagine start is 30 and limit is 15. It means that we already have page 1 and 2, now we need page 3
            val page = start / limit + 1
            v1Source.requestRecipes(page, limit).map { modelMapper.toRecipeSummaryInfo(it) }
        }
    }

    override suspend fun requestRecipeInfo(slug: String): FullRecipeInfo = when (getVersion()) {
        ServerVersion.V0 -> modelMapper.toFullRecipeInfo(v0Source.requestRecipeInfo(slug))
        ServerVersion.V1 -> modelMapper.toFullRecipeInfo(v1Source.requestRecipeInfo(slug))
    }

    override suspend fun parseRecipeFromURL(
        parseRecipeURLInfo: ParseRecipeURLInfo,
    ): String = when (getVersion()) {
        ServerVersion.V0 -> v0Source.parseRecipeFromURL(modelMapper.toV0Request(parseRecipeURLInfo))
        ServerVersion.V1 -> v1Source.parseRecipeFromURL(modelMapper.toV1Request(parseRecipeURLInfo))
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