package gq.kirmanak.mealient.database.recipe

import androidx.paging.PagingSource
import androidx.room.*
import gq.kirmanak.mealient.database.recipe.entity.*

@Dao
interface RecipeDao {
    @Query("SELECT * FROM tags")
    suspend fun queryAllTags(): List<TagEntity>

    @Query("SELECT * FROM categories")
    suspend fun queryAllCategories(): List<CategoryEntity>

    @Query("SELECT * FROM recipe_summaries ORDER BY date_added DESC")
    fun queryRecipesByPages(): PagingSource<Int, RecipeSummaryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipeSummaryEntity: RecipeSummaryEntity)

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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipeInstructions(instructions: List<RecipeInstructionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipeIngredients(ingredients: List<RecipeIngredientEntity>)

    @Transaction
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH) // The lint is wrong, the columns are actually used
    @Query("SELECT * FROM recipe JOIN recipe_summaries ON recipe.remote_id = recipe_summaries.remote_id JOIN recipe_ingredient ON recipe_ingredient.recipe_id = recipe.remote_id JOIN recipe_instruction ON recipe_instruction.recipe_id = recipe.remote_id WHERE recipe.remote_id = :recipeId")
    suspend fun queryFullRecipeInfo(recipeId: String): FullRecipeEntity?

    @Query("DELETE FROM recipe_ingredient WHERE recipe_id = :recipeId")
    suspend fun deleteRecipeIngredients(recipeId: String)

    @Query("DELETE FROM recipe_instruction WHERE recipe_id = :recipeId")
    suspend fun deleteRecipeInstructions(recipeId: String)
}