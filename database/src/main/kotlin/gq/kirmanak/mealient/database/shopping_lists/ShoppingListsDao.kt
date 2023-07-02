package gq.kirmanak.mealient.database.shopping_lists

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListItemEntity
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListItemRecipeReferenceEntity
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListWithItems
import kotlinx.coroutines.flow.Flow

@Dao
internal interface ShoppingListsDao {

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
    fun queryShoppingListWithItems(id: String): Flow<ShoppingListWithItems>
}