package gq.kirmanak.mealient.data.recipes.impl

import androidx.annotation.VisibleForTesting
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.LoadType.PREPEND
import androidx.paging.LoadType.REFRESH
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import gq.kirmanak.mealient.data.recipes.db.RecipeStorage
import gq.kirmanak.mealient.data.recipes.db.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.data.recipes.network.RecipeDataSource
import kotlinx.coroutines.CancellationException
import timber.log.Timber
import javax.inject.Inject

@ExperimentalPagingApi
class RecipesRemoteMediator @Inject constructor(
    private val storage: RecipeStorage,
    private val network: RecipeDataSource,
    private val pagingSourceFactory: RecipePagingSourceFactory,
) : RemoteMediator<Int, RecipeSummaryEntity>() {

    @VisibleForTesting
    var lastRequestEnd: Int = 0

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RecipeSummaryEntity>
    ): MediatorResult {
        Timber.v("load() called with: lastRequestEnd = $lastRequestEnd, loadType = $loadType, state = $state")

        if (loadType == PREPEND) {
            Timber.i("load: early exit, PREPEND isn't supported")
            return MediatorResult.Success(endOfPaginationReached = true)
        }

        val start = if (loadType == REFRESH) 0 else lastRequestEnd
        val limit = if (loadType == REFRESH) state.config.initialLoadSize else state.config.pageSize

        val count: Int = try {
            val recipes = network.requestRecipes(start, limit)
            if (loadType == REFRESH) storage.refreshAll(recipes)
            else storage.saveRecipes(recipes)
            recipes.size
        } catch (e: CancellationException) {
            throw e
        } catch (e: Throwable) {
            Timber.e(e, "Can't load recipes")
            return MediatorResult.Error(e)
        }

        // After something is inserted into DB the paging sources have to be invalidated
        // But for some reason Room/Paging library don't do it automatically
        // Here we invalidate them manually.
        // Read that trick here https://github.com/android/architecture-components-samples/issues/889#issuecomment-880847858
        pagingSourceFactory.invalidate()

        Timber.d("load: expectedCount = $limit, received $count")
        lastRequestEnd = start + count
        return MediatorResult.Success(endOfPaginationReached = count < limit)
    }
}