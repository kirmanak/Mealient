package gq.kirmanak.mealient.data.recipes.impl

import androidx.paging.ExperimentalPagingApi
import androidx.paging.InvalidatingPagingSourceFactory
import androidx.paging.Pager
import androidx.paging.PagingConfig
import gq.kirmanak.mealient.data.recipes.RecipeRepo
import gq.kirmanak.mealient.data.recipes.db.RecipeStorage
import gq.kirmanak.mealient.data.recipes.db.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.data.recipes.network.RecipeDataSource
import kotlinx.coroutines.CancellationException
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalPagingApi::class)
@Singleton
class RecipeRepoImpl @Inject constructor(
    private val mediator: RecipesRemoteMediator,
    private val storage: RecipeStorage,
    private val pagingSourceFactory: InvalidatingPagingSourceFactory<Int, RecipeSummaryEntity>,
    private val dataSource: RecipeDataSource,
) : RecipeRepo {
    override fun createPager(): Pager<Int, RecipeSummaryEntity> {
        Timber.v("createPager() called")
        val pagingConfig = PagingConfig(pageSize = 5, enablePlaceholders = true)
        return Pager(
            config = pagingConfig,
            remoteMediator = mediator,
            pagingSourceFactory = pagingSourceFactory
        )
    }

    override suspend fun clearLocalData() {
        Timber.v("clearLocalData() called")
        storage.clearAllLocalData()
    }

    override suspend fun loadRecipeInfo(recipeId: Long, recipeSlug: String): FullRecipeInfo {
        Timber.v("loadRecipeInfo() called with: recipeId = $recipeId, recipeSlug = $recipeSlug")

        try {
            storage.saveRecipeInfo(dataSource.requestRecipeInfo(recipeSlug))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Throwable) {
            Timber.e(e, "loadRecipeInfo: can't update full recipe info")
        }

        return storage.queryRecipeInfo(recipeId)
    }
}