package gq.kirmanak.mealient.data.recipes.impl

import androidx.annotation.VisibleForTesting
import androidx.paging.*
import androidx.paging.LoadType.PREPEND
import androidx.paging.LoadType.REFRESH
import gq.kirmanak.mealient.data.recipes.db.RecipeStorage
import gq.kirmanak.mealient.data.recipes.network.RecipeDataSource
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.datasource.runCatchingExceptCancel
import gq.kirmanak.mealient.logging.Logger
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalPagingApi::class)
@Singleton
class RecipesRemoteMediator @Inject constructor(
    private val storage: RecipeStorage,
    private val network: RecipeDataSource,
    private val pagingSourceFactory: RecipePagingSourceFactory,
    private val logger: Logger,
) : RemoteMediator<Int, RecipeSummaryEntity>() {

    @VisibleForTesting
    var lastRequestEnd: Int = 0

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RecipeSummaryEntity>
    ): MediatorResult {
        logger.v { "load() called with: lastRequestEnd = $lastRequestEnd, loadType = $loadType, state = $state" }

        if (loadType == PREPEND) {
            logger.i { "load: early exit, PREPEND isn't supported" }
            return MediatorResult.Success(endOfPaginationReached = true)
        }

        val start = if (loadType == REFRESH) 0 else lastRequestEnd
        val limit = if (loadType == REFRESH) state.config.initialLoadSize else state.config.pageSize

        val count: Int = runCatchingExceptCancel {
            val recipes = network.requestRecipes(start, limit)
            if (loadType == REFRESH) storage.refreshAll(recipes)
            else storage.saveRecipes(recipes)
            recipes.size
        }.getOrElse {
            logger.e(it) { "load: can't load recipes" }
            return MediatorResult.Error(it)
        }

        // After something is inserted into DB the paging sources have to be invalidated
        // But for some reason Room/Paging library don't do it automatically
        // Here we invalidate them manually.
        // Read that trick here https://github.com/android/architecture-components-samples/issues/889#issuecomment-880847858
        pagingSourceFactory.invalidate()

        logger.d { "load: expectedCount = $limit, received $count" }
        lastRequestEnd = start + count
        return MediatorResult.Success(endOfPaginationReached = count < limit)
    }
}