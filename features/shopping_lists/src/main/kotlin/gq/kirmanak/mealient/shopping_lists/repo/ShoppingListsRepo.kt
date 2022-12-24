package gq.kirmanak.mealient.shopping_lists.repo

import androidx.paging.Pager
import gq.kirmanak.mealient.database.recipe.entity.ShoppingListEntity

interface ShoppingListsRepo {

    fun createPager(): Pager<Int, ShoppingListEntity>
}