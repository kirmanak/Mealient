package gq.kirmanak.mealient.data.recipes.db

import androidx.paging.PagingSource
import gq.kirmanak.mealient.database.recipe.entity.FullRecipeInfo
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.datasource.models.GetRecipeResponse
import gq.kirmanak.mealient.datasource.models.GetRecipeSummaryResponse

interface RecipeStorage {
    suspend fun saveRecipes(recipes: List<GetRecipeSummaryResponse>)

    fun queryRecipes(): PagingSource<Int, RecipeSummaryEntity>

    suspend fun refreshAll(recipes: List<GetRecipeSummaryResponse>)

    suspend fun clearAllLocalData()

    suspend fun saveRecipeInfo(recipe: GetRecipeResponse)

    suspend fun queryRecipeInfo(recipeId: Long): FullRecipeInfo
}