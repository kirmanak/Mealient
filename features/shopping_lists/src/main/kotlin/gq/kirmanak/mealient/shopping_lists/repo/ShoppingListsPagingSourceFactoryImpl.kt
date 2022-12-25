package gq.kirmanak.mealient.shopping_lists.repo

import androidx.paging.InvalidatingPagingSourceFactory
import androidx.paging.PagingSource
import gq.kirmanak.mealient.database.shopping_lists.ShoppingListsStorage
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListEntity
import gq.kirmanak.mealient.logging.Logger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShoppingListsPagingSourceFactoryImpl @Inject constructor(
    private val storage: ShoppingListsStorage,
    private val logger: Logger,
) : ShoppingListsPagingSourceFactory {

    private val delegate = InvalidatingPagingSourceFactory {
        logger.v { "Creating new paging source" }
        storage.queryShoppingLists()
    }

    override fun invalidate() = delegate.invalidate()

    override fun invoke(): PagingSource<Int, ShoppingListEntity> = delegate.invoke()
}