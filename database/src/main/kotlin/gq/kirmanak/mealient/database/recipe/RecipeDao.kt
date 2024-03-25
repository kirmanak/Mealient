package gq.kirmanak.mealient.database.recipe

import androidx.paging.PagingSource
import androidx.room.*
import gq.kirmanak.mealient.database.recipe.entity.*

@Dao
internal interface RecipeDao {
    @Query("SELECT * FROM recipe_summaries ORDER BY recipe_summaries_date_added DESC")
    fun queryRecipesByPages(): PagingSource<Int, RecipeSummaryEntity>

    @Query("SELECT * FROM recipe_summaries WHERE recipe_summaries_name LIKE '%' || :query || '%' ORDER BY recipe_summaries_date_added DESC")
    fun queryRecipesByPages(query: String): PagingSource<Int, RecipeSummaryEntity>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipeSummaries(recipeSummaryEntity: Iterable<RecipeSummaryEntity>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipeSettings(recipeSettingsEntity: RecipeSettingsEntity)

    @Transaction
    @Query("DELETE FROM recipe_summaries")
    suspend fun removeAllRecipes()

    @Query("SELECT * FROM recipe_summaries ORDER BY recipe_summaries_date_added DESC")
    suspend fun queryAllRecipes(): List<RecipeSummaryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipe: List<RecipeEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipeInstructions(instructions: List<RecipeInstructionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipeIngredients(ingredients: List<RecipeIngredientEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredientToInstructionEntities(entities: List<RecipeIngredientToInstructionEntity>)

    @Transaction
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH) // The lint is wrong, the columns are actually used
    @Query(
        "SELECT * FROM recipe " +
                "JOIN recipe_summaries USING(recipe_id) " +
                "LEFT JOIN recipe_ingredient USING(recipe_id) " +
                "LEFT JOIN recipe_instruction USING(recipe_id) " +
                "LEFT JOIN recipe_ingredient_to_instruction USING(recipe_id) " +
                "WHERE recipe.recipe_id = :recipeId"
    )
    suspend fun queryFullRecipeInfo(recipeId: String): RecipeWithSummaryAndIngredientsAndInstructions?

    @Query("DELETE FROM recipe_ingredient WHERE recipe_id IN (:recipeIds)")
    suspend fun deleteRecipeIngredients(vararg recipeIds: String)

    @Query("DELETE FROM recipe_instruction WHERE recipe_id IN (:recipeIds)")
    suspend fun deleteRecipeInstructions(vararg recipeIds: String)

    @Query("DELETE FROM recipe_ingredient_to_instruction WHERE recipe_id IN (:recipeIds)")
    suspend fun deleteRecipeIngredientToInstructions(vararg recipeIds: String)

    @Query("UPDATE recipe_summaries SET recipe_summaries_is_favorite = 1 WHERE recipe_summaries_slug IN (:favorites)")
    suspend fun setFavorite(favorites: List<String>)

    @Query("UPDATE recipe_summaries SET recipe_summaries_is_favorite = 0 WHERE recipe_summaries_slug NOT IN (:favorites)")
    suspend fun setNonFavorite(favorites: List<String>)

    @Delete
    suspend fun deleteRecipe(entity: RecipeSummaryEntity)
}