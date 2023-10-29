package gq.kirmanak.mealient.data.network

import gq.kirmanak.mealient.data.add.AddRecipeDataSource
import gq.kirmanak.mealient.data.recipes.network.RecipeDataSource
import gq.kirmanak.mealient.data.share.ParseRecipeDataSource
import gq.kirmanak.mealient.datasource.MealieDataSource
import gq.kirmanak.mealient.datasource.models.AddRecipeInfo
import gq.kirmanak.mealient.datasource.models.FullRecipeInfo
import gq.kirmanak.mealient.datasource.models.ParseRecipeURLInfo
import gq.kirmanak.mealient.datasource.models.RecipeSummaryInfo
import gq.kirmanak.mealient.model_mapper.ModelMapper
import javax.inject.Inject

class MealieDataSourceWrapper @Inject constructor(
    private val dataSource: MealieDataSource,
    private val modelMapper: ModelMapper,
) : AddRecipeDataSource, RecipeDataSource, ParseRecipeDataSource {

    override suspend fun addRecipe(recipe: AddRecipeInfo): String {
        val slug = dataSource.createRecipe(modelMapper.toV1CreateRequest(recipe))
        dataSource.updateRecipe(slug, modelMapper.toV1UpdateRequest(recipe))
        return slug
    }

    override suspend fun requestRecipes(
        start: Int,
        limit: Int,
    ): List<RecipeSummaryInfo> {
        // Imagine start is 30 and limit is 15. It means that we already have page 1 and 2, now we need page 3
        val page = start / limit + 1
        return dataSource.requestRecipes(page, limit).map { modelMapper.toRecipeSummaryInfo(it) }
    }

    override suspend fun requestRecipeInfo(slug: String): FullRecipeInfo {
        return modelMapper.toFullRecipeInfo(dataSource.requestRecipeInfo(slug))
    }

    override suspend fun parseRecipeFromURL(parseRecipeURLInfo: ParseRecipeURLInfo): String {
        return dataSource.parseRecipeFromURL(modelMapper.toV1Request(parseRecipeURLInfo))
    }

    override suspend fun getFavoriteRecipes(): List<String> {
        return dataSource.requestUserInfo().favoriteRecipes
    }

    override suspend fun updateIsRecipeFavorite(recipeSlug: String, isFavorite: Boolean) {
        val userId = dataSource.requestUserInfo().id
        if (isFavorite) {
            dataSource.addFavoriteRecipe(userId, recipeSlug)
        } else {
            dataSource.removeFavoriteRecipe(userId, recipeSlug)
        }
    }

    override suspend fun deleteRecipe(recipeSlug: String) {
        dataSource.deleteRecipe(recipeSlug)
    }
}