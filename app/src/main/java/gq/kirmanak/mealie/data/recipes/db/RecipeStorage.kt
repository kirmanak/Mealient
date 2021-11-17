package gq.kirmanak.mealie.data.recipes.db

import androidx.paging.PagingSource
import gq.kirmanak.mealie.data.recipes.db.entity.RecipeSummaryEntity
import gq.kirmanak.mealie.data.recipes.network.response.GetRecipeResponse
import gq.kirmanak.mealie.data.recipes.network.response.GetRecipeSummaryResponse

interface RecipeStorage {
    suspend fun saveRecipes(recipes: List<GetRecipeSummaryResponse>)

    fun queryRecipes(): PagingSource<Int, RecipeSummaryEntity>

    suspend fun refreshAll(recipes: List<GetRecipeSummaryResponse>)

    suspend fun clearAllLocalData()

    suspend fun saveRecipeInfo(recipe: GetRecipeResponse)
}