package gq.kirmanak.mealient.shopping_lists.repo

import androidx.paging.PagingSource
import gq.kirmanak.mealient.database.recipe.entity.ShoppingListEntity

interface ShoppingListsPagingSourceFactory : () -> PagingSource<Int, ShoppingListEntity> {

    fun invalidate()
}