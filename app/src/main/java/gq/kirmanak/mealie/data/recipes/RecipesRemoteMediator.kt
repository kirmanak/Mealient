package gq.kirmanak.mealie.data.recipes

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.LoadType.*
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import gq.kirmanak.mealie.data.recipes.db.RecipeEntity
import gq.kirmanak.mealie.data.recipes.db.RecipeStorage
import gq.kirmanak.mealie.data.recipes.network.RecipeDataSource
import javax.inject.Inject

@ExperimentalPagingApi
class RecipesRemoteMediator @Inject constructor(
    private val storage: RecipeStorage,
    private val network: RecipeDataSource
) : RemoteMediator<Int, RecipeEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RecipeEntity>
    ): MediatorResult {
        val pageSize = state.config.pageSize
        val closestPage = state.anchorPosition?.let { state.closestPageToPosition(it) }
        val start = when (loadType) {
            REFRESH -> 0
            PREPEND -> closestPage?.prevKey ?: 0
            APPEND -> closestPage?.nextKey ?: 0
        }
        val end = when (loadType) {
            REFRESH -> pageSize
            PREPEND, APPEND -> start + pageSize
        }

        val recipes = try {
            network.requestRecipes(start = start, end = end)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }

        try {
            when (loadType) {
                REFRESH -> storage.refreshAll(recipes)
                PREPEND, APPEND -> storage.saveRecipes(recipes)
            }
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
        val expectedCount = end - start
        val isEndReached = recipes.size < expectedCount
        return MediatorResult.Success(isEndReached)
    }
}