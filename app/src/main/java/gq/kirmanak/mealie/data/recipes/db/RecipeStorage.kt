package gq.kirmanak.mealie.data.recipes.db

import androidx.paging.PagingSource
import gq.kirmanak.mealie.data.recipes.network.GetRecipeSummaryResponse

interface RecipeStorage {
    suspend fun saveRecipes(recipes: List<GetRecipeSummaryResponse>)

    fun queryRecipes(): PagingSource<Int, RecipeEntity>

    suspend fun refreshAll(recipes: List<GetRecipeSummaryResponse>)
}