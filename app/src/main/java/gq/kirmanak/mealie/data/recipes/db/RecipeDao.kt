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

    @Query("SELECT * FROM recipes")
    fun queryRecipesByPages(): PagingSource<Int, RecipeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipeEntity: RecipeEntity): Long

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

    @Query("DELETE FROM recipes")
    suspend fun removeAllRecipes()

    @Query("SELECT * FROM recipes")
    suspend fun queryAllRecipes(): List<RecipeEntity>
}