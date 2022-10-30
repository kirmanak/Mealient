package gq.kirmanak.mealient.database.recipe

import androidx.paging.PagingSource
import androidx.room.*
import gq.kirmanak.mealient.database.recipe.entity.*

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipe_summaries ORDER BY date_added DESC")
    fun queryRecipesByPages(): PagingSource<Int, RecipeSummaryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipeSummaryEntity: RecipeSummaryEntity)

    @Query("DELETE FROM recipe_summaries")
    suspend fun removeAllRecipes()

    @Query("SELECT * FROM recipe_summaries ORDER BY date_updated DESC")
    suspend fun queryAllRecipes(): List<RecipeSummaryEntity>

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