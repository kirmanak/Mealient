package gq.kirmanak.mealient.database.recipe

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import gq.kirmanak.mealient.database.recipe.entity.ShoppingListEntity

@Dao
interface ShoppingListsDao {

    @Query("SELECT * FROM shopping_lists")
    fun queryShoppingListsByPages(): PagingSource<Int, ShoppingListEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingLists(shoppingLists: List<ShoppingListEntity>)
}