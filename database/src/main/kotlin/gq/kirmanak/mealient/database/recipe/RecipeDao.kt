package gq.kirmanak.mealient.database.recipe

import androidx.paging.PagingSource
import androidx.room.*
import gq.kirmanak.mealient.database.recipe.entity.*

@Dao
internal interface RecipeDao {
    @Query("SELECT * FROM recipe_summaries ORDER BY date_added DESC")
    fun queryRecipesByPages(): PagingSource<Int, RecipeSummaryEntity>

    @Query("SELECT * FROM recipe_summaries WHERE recipe_summaries.name LIKE '%' || :query || '%' ORDER BY date_added DESC")
    fun queryRecipesByPages(query: String): PagingSource<Int, RecipeSummaryEntity>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipeSummaries(recipeSummaryEntity: Iterable<RecipeSummaryEntity>)

    @Transaction
    @Query("DELETE FROM recipe_summaries")
    suspend fun removeAllRecipes()

    @Query("SELECT * FROM recipe_summaries ORDER BY date_updated DESC")
    suspend fun queryAllRecipes(): List<RecipeSummaryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipe: List<RecipeEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipeInstructions(instructions: List<RecipeInstructionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipeIngredients(ingredients: List<RecipeIngredientEntity>)

    @Transaction
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH) // The lint is wrong, the columns are actually used
    @Query(
        "SELECT * FROM recipe " +
                "JOIN recipe_summaries ON recipe.remote_id = recipe_summaries.remote_id " +
                "JOIN recipe_ingredient ON recipe_ingredient.recipe_id = recipe.remote_id " +
                "JOIN recipe_instruction ON recipe_instruction.recipe_id = recipe.remote_id " +
                "WHERE recipe.remote_id = :recipeId"
    )
    suspend fun queryFullRecipeInfo(recipeId: String): FullRecipeEntity?

    @Query("DELETE FROM recipe_ingredient WHERE recipe_id IN (:recipeIds)")
    suspend fun deleteRecipeIngredients(vararg recipeIds: String)

    @Query("DELETE FROM recipe_instruction WHERE recipe_id IN (:recipeIds)")
    suspend fun deleteRecipeInstructions(vararg recipeIds: String)

    @Query("UPDATE recipe_summaries SET is_favorite = 1 WHERE slug IN (:favorites)")
    suspend fun setFavorite(favorites: List<String>)

    @Query("UPDATE recipe_summaries SET is_favorite = 0 WHERE slug NOT IN (:favorites)")
    suspend fun setNonFavorite(favorites: List<String>)

    @Delete
    suspend fun deleteRecipe(entity: RecipeSummaryEntity)
}