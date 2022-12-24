package gq.kirmanak.mealient.shopping_lists.repo

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import gq.kirmanak.mealient.database.recipe.entity.ShoppingListEntity
import gq.kirmanak.mealient.datasource.runCatchingExceptCancel
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.shopping_lists.models.toShoppingListEntities
import gq.kirmanak.mealient.shopping_lists.network.ShoppingListsDataSource
import gq.kirmanak.mealient.shopping_lists.storage.ShoppingListsStorage
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

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ShoppingListEntity>,
    ): MediatorResult {
        logger.v { "load() called with: loadType = $loadType, state = $state" }

        // TODO don't think that prevKey/nextKey are set correctly
        val page = when (loadType) {
            LoadType.REFRESH -> 1
            LoadType.PREPEND -> state.pages.firstOrNull()?.prevKey
            LoadType.APPEND -> state.pages.lastOrNull()?.nextKey
        } ?: return MediatorResult.Success(endOfPaginationReached = true)

        val perPage = when (loadType) {
            LoadType.REFRESH -> state.config.initialLoadSize
            else -> state.config.pageSize
        }

        val response = runCatchingExceptCancel {
            dataSource.getPage(page, perPage)
        }.fold(
            onSuccess = {
                logger.d { "load: expected page $page with $perPage items, got page ${it.page} with ${it.items.size} items. Totals are ${it.totalPages}/${it.totalItems}." }
                it
            },
            onFailure = {
                logger.e(it) { "load: can't load shopping lists" }
                return MediatorResult.Error(it)
            }
        )

        val shoppingLists = response.toShoppingListEntities()

        runCatchingExceptCancel {
            storage.saveShoppingLists(shoppingLists)
        }.fold(
            onSuccess = {
                logger.d { "load: inserted items" }
            },
            onFailure = {
                logger.e(it) { "load: can't insert shopping lists" }
                return MediatorResult.Error(it)
            }
        )

        sourceFactory.invalidate()

        return MediatorResult.Success(endOfPaginationReached = response.page == response.totalPages)

    }
}