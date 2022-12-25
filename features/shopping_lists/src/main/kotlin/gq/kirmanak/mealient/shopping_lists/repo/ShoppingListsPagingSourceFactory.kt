package gq.kirmanak.mealient.shopping_lists.repo

import androidx.paging.PagingSource
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListEntity

interface ShoppingListsPagingSourceFactory : () -> PagingSource<Int, ShoppingListEntity> {

    fun invalidate()
}