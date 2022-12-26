package gq.kirmanak.mealient.database.recipe

import androidx.paging.PagingSource
import androidx.room.withTransaction
import gq.kirmanak.mealient.database.AppDb
import gq.kirmanak.mealient.database.recipe.entity.FullRecipeEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeIngredientEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeInstructionEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.logging.Logger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class RecipeStorageImpl @Inject constructor(
    private val db: AppDb,
    private val logger: Logger,
    private val recipeDao: RecipeDao,
) : RecipeStorage {

    override suspend fun saveRecipes(recipes: List<RecipeSummaryEntity>) {
        logger.v { "saveRecipes() called with $recipes" }
        recipeDao.insertRecipeSummaries(recipes)
    }

    override fun queryRecipes(query: String?): PagingSource<Int, RecipeSummaryEntity> {
        logger.v { "queryRecipes() called with: query = $query" }
        return if (query == null) recipeDao.queryRecipesByPages()
        else recipeDao.queryRecipesByPages(query)
    }

    override suspend fun refreshAll(recipes: List<RecipeSummaryEntity>) {
        logger.v { "refreshAll() called with: recipes = $recipes" }
        db.withTransaction {
            recipeDao.removeAllRecipes()
            saveRecipes(recipes)
        }
    }

    override suspend fun clearAllLocalData() {
        logger.v { "clearAllLocalData() called" }
        recipeDao.removeAllRecipes()
    }

    override suspend fun saveRecipeInfo(
        recipe: RecipeEntity,
        ingredients: List<RecipeIngredientEntity>,
        instructions: List<RecipeInstructionEntity>
    ) {
        logger.v { "saveRecipeInfo() called with: recipe = $recipe" }
        db.withTransaction {
            recipeDao.insertRecipe(recipe)

            recipeDao.deleteRecipeIngredients(recipe.remoteId)
            recipeDao.insertRecipeIngredients(ingredients)

            recipeDao.deleteRecipeInstructions(recipe.remoteId)
            recipeDao.insertRecipeInstructions(instructions)
        }
    }

    override suspend fun queryRecipeInfo(recipeId: String): FullRecipeEntity? {
        logger.v { "queryRecipeInfo() called with: recipeId = $recipeId" }
        val fullRecipeInfo = recipeDao.queryFullRecipeInfo(recipeId)
        logger.v { "queryRecipeInfo() returned: $fullRecipeInfo" }
        return fullRecipeInfo
    }

    override suspend fun updateFavoriteRecipes(favorites: List<String>) {
        logger.v { "updateFavoriteRecipes() called with: favorites = $favorites" }
        db.withTransaction {
            recipeDao.setFavorite(favorites)
            recipeDao.setNonFavorite(favorites)
        }
    }

    override suspend fun deleteRecipe(entity: RecipeSummaryEntity) {
        logger.v { "deleteRecipeBySlug() called with: entity = $entity" }
        recipeDao.deleteRecipe(entity)
    }
}