package gq.kirmanak.mealie.data.recipes.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecipeDao {
    @Query("SELECT * FROM tags")
    suspend fun queryAllTags(): List<TagEntity>

    @Query("SELECT * FROM categories")
    suspend fun queryAllCategories(): List<CategoryEntity>

    @Query("SELECT * FROM recipe_summaries ORDER BY date_added DESC")
    fun queryRecipesByPages(): PagingSource<Int, RecipeSummaryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipeSummaryEntity: RecipeSummaryEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTag(tagEntity: TagEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTagRecipeEntity(tagRecipeEntity: TagRecipeEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategory(categoryEntity: CategoryEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategoryRecipeEntity(categoryRecipeEntity: CategoryRecipeEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTagRecipeEntities(tagRecipeEntities: Set<TagRecipeEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategoryRecipeEntities(categoryRecipeEntities: Set<CategoryRecipeEntity>)

    @Query("DELETE FROM recipe_summaries")
    suspend fun removeAllRecipes()

    @Query("DELETE FROM tags")
    suspend fun removeAllTags()

    @Query("DELETE FROM categories")
    suspend fun removeAllCategories()

    @Query("SELECT * FROM recipe_summaries ORDER BY date_updated DESC")
    suspend fun queryAllRecipes(): List<RecipeSummaryEntity>

    @Query("SELECT * FROM category_recipe")
    suspend fun queryAllCategoryRecipes(): List<CategoryRecipeEntity>

    @Query("SELECT * FROM tag_recipe")
    suspend fun queryAllTagRecipes(): List<TagRecipeEntity>
}