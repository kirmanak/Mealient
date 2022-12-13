package gq.kirmanak.mealient.data.recipes.db

import androidx.paging.PagingSource
import gq.kirmanak.mealient.data.recipes.network.FullRecipeInfo
import gq.kirmanak.mealient.database.recipe.entity.FullRecipeEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity

interface RecipeStorage {
    suspend fun saveRecipes(recipes: List<RecipeSummaryEntity>)

    fun queryRecipes(query: String?): PagingSource<Int, RecipeSummaryEntity>

    suspend fun refreshAll(recipes: List<RecipeSummaryEntity>)

    suspend fun clearAllLocalData()

    suspend fun saveRecipeInfo(recipe: FullRecipeInfo)

    suspend fun queryRecipeInfo(recipeId: String): FullRecipeEntity?

    suspend fun updateFavoriteRecipes(favorites: List<String>)
}