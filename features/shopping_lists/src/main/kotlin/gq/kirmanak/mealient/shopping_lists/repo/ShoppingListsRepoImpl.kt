package gq.kirmanak.mealient.shopping_lists.repo

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListEntity
import gq.kirmanak.mealient.logging.Logger
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalPagingApi::class)
@Singleton
class ShoppingListsRepoImpl @Inject constructor(
    private val remoteMediator: ShoppingListsRemoteMediator,
    private val sourceFactory: ShoppingListsPagingSourceFactory,
    private val logger: Logger,
) : ShoppingListsRepo {

    override fun createPager(): Pager<Int, ShoppingListEntity> {
        logger.v { "createPager() called" }
        val pagingConfig = PagingConfig(
            pageSize = LOAD_PAGE_SIZE,
            enablePlaceholders = true,
            initialLoadSize = INITIAL_LOAD_PAGE_SIZE,
        )
        return Pager(
            config = pagingConfig,
            remoteMediator = remoteMediator,
            pagingSourceFactory = sourceFactory,
        )
    }

    companion object {
        private const val LOAD_PAGE_SIZE = 50
        private const val INITIAL_LOAD_PAGE_SIZE = LOAD_PAGE_SIZE * 3
    }
}