package gq.kirmanak.mealient.shopping_lists.repo

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import gq.kirmanak.mealient.database.shopping_lists.ShoppingListsStorage
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListEntity
import gq.kirmanak.mealient.datasource.runCatchingExceptCancel
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.shopping_lists.models.toShoppingListEntities
import gq.kirmanak.mealient.shopping_lists.network.ShoppingListsDataSource
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalPagingApi::class)
@Singleton
class ShoppingListsRemoteMediator @Inject constructor(
    private val storage: ShoppingListsStorage,
    private val dataSource: ShoppingListsDataSource,
    private val sourceFactory: ShoppingListsPagingSourceFactory,
    private val logger: Logger,
) : RemoteMediator<Int, ShoppingListEntity>() {

    private var lastRequestEnd = 0

    override suspend fun initialize(): InitializeAction = InitializeAction.SKIP_INITIAL_REFRESH

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ShoppingListEntity>,
    ): MediatorResult {
        logger.v { "load() called with: lastRequestEnd = $lastRequestEnd, loadType = $loadType, state = $state" }

        val startAtIndex = when (loadType) {
            LoadType.REFRESH -> 0
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> lastRequestEnd
        }

        val perPage = when (loadType) {
            LoadType.REFRESH -> state.config.initialLoadSize
            else -> state.config.pageSize
        }

        val page = startAtIndex / perPage + 1

        logger.d { "load: starting at $startAtIndex index, loading $perPage item from page $page" }

        val response = runCatchingExceptCancel {
            dataSource.getPage(page, perPage)
        }.fold(
            onSuccess = {
                logger.d { "load: got page ${it.page} with ${it.items.size} items. Totals are ${it.totalPages}/${it.totalItems}." }
                it
            },
            onFailure = {
                logger.e(it) { "load: can't load shopping lists" }
                return MediatorResult.Error(it)
            }
        )

        val shoppingLists = response.toShoppingListEntities()

        runCatchingExceptCancel {
            if (loadType == LoadType.REFRESH) {
                storage.refreshShoppingLists(shoppingLists)
            } else {
                storage.saveShoppingLists(shoppingLists)
            }
        }.fold(
            onSuccess = {
                logger.d { "load: ${if (loadType == LoadType.REFRESH) "refreshed" else "appended"} items" }
            },
            onFailure = {
                logger.e(it) { "load: can't insert shopping lists" }
                return MediatorResult.Error(it)
            }
        )

        sourceFactory.invalidate()

        lastRequestEnd = startAtIndex + shoppingLists.size

        return MediatorResult.Success(endOfPaginationReached = response.page == response.totalPages)

    }
}