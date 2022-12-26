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
                "JOIN shopping_list_item ON shopping_list_item.shopping_list_id = shopping_lists.remote_id " +
                "JOIN shopping_list_item_recipe_reference ON shopping_list_item_recipe_reference.shopping_list_item_id = shopping_list_item.remote_id " +
                "JOIN recipe ON recipe.remote_id = shopping_list_item_recipe_reference.recipe_id " +
                "JOIN recipe_summaries ON recipe_summaries.remote_id = shopping_list_item_recipe_reference.recipe_id " +
                "JOIN recipe_ingredient ON recipe_ingredient.recipe_id = shopping_list_item_recipe_reference.recipe_id " +
                "WHERE shopping_lists.remote_id = :id"
    )
    suspend fun queryFullShoppingList(id: String): ShoppingListWithItems
}