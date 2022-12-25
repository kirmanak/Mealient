package gq.kirmanak.mealient.database.recipe

import androidx.paging.PagingSource
import gq.kirmanak.mealient.database.recipe.entity.FullRecipeEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeIngredientEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeInstructionEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity

interface RecipeStorage {
    suspend fun saveRecipes(recipes: List<RecipeSummaryEntity>)

    fun queryRecipes(query: String?): PagingSource<Int, RecipeSummaryEntity>

    suspend fun refreshAll(recipes: List<RecipeSummaryEntity>)

    suspend fun clearAllLocalData()

    suspend fun saveRecipeInfo(
        recipe: RecipeEntity,
        ingredients: List<RecipeIngredientEntity>,
        instructions: List<RecipeInstructionEntity>
    )

    suspend fun queryRecipeInfo(recipeId: String): FullRecipeEntity?

    suspend fun updateFavoriteRecipes(favorites: List<String>)

    suspend fun deleteRecipe(entity: RecipeSummaryEntity)
}