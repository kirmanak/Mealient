package gq.kirmanak.mealient.database.shopping_lists

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListEntity
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListItemEntity
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListItemRecipeReferenceEntity
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListWithItems

@Dao
internal interface ShoppingListsDao {

    @Query("SELECT * FROM shopping_lists")
    fun queryShoppingListsByPages(): PagingSource<Int, ShoppingListEntity>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingLists(shoppingLists: List<ShoppingListEntity>)

    @Query("DELETE FROM shopping_lists")
    suspend fun deleteAllShoppingLists()

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingListItems(items: List<ShoppingListItemEntity>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingListItemRecipeReferences(items: List<ShoppingListItemRecipeReferenceEntity>)

    @Query(
        "SELECT * FROM shopping_lists " +
                "LEFT JOIN shopping_list_item USING(shopping_list_id) " +
                "LEFT JOIN shopping_list_item_recipe_reference USING(shopping_list_item_id) " +
                "LEFT JOIN recipe USING (recipe_id) " +
                "LEFT JOIN recipe_summaries USING (recipe_id)  " +
                "LEFT JOIN recipe_ingredient USING (recipe_id)  " +
                "WHERE shopping_lists.shopping_list_id = :id"
    )
    suspend fun queryFullShoppingList(id: String): ShoppingListWithItems
}