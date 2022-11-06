package gq.kirmanak.mealient.data.recipes.db

import androidx.paging.PagingSource
import gq.kirmanak.mealient.data.recipes.network.FullRecipeInfo
import gq.kirmanak.mealient.data.recipes.network.RecipeSummaryInfo
import gq.kirmanak.mealient.database.recipe.entity.FullRecipeEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity

interface RecipeStorage {
    suspend fun saveRecipes(recipes: List<RecipeSummaryInfo>)

    fun queryRecipes(): PagingSource<Int, RecipeSummaryEntity>

    suspend fun refreshAll(recipes: List<RecipeSummaryInfo>)

    suspend fun clearAllLocalData()

    suspend fun saveRecipeInfo(recipe: FullRecipeInfo)

    suspend fun queryRecipeInfo(recipeId: String): FullRecipeEntity?
}