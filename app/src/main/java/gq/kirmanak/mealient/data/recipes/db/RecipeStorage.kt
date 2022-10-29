package gq.kirmanak.mealient.data.recipes.db

import androidx.paging.PagingSource
import gq.kirmanak.mealient.data.recipes.network.RecipeSummaryInfo
import gq.kirmanak.mealient.database.recipe.entity.FullRecipeInfo
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.datasource.models.GetRecipeResponse

interface RecipeStorage {
    suspend fun saveRecipes(recipes: List<RecipeSummaryInfo>)

    fun queryRecipes(): PagingSource<Int, RecipeSummaryEntity>

    suspend fun refreshAll(recipes: List<RecipeSummaryInfo>)

    suspend fun clearAllLocalData()

    suspend fun saveRecipeInfo(recipe: GetRecipeResponse)

    suspend fun queryRecipeInfo(recipeId: String): FullRecipeInfo
}