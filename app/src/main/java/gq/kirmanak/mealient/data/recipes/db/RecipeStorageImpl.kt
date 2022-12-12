package gq.kirmanak.mealient.data.recipes.db

import androidx.paging.PagingSource
import androidx.room.withTransaction
import gq.kirmanak.mealient.data.recipes.network.FullRecipeInfo
import gq.kirmanak.mealient.database.AppDb
import gq.kirmanak.mealient.database.recipe.RecipeDao
import gq.kirmanak.mealient.database.recipe.entity.FullRecipeEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.extensions.toRecipeEntity
import gq.kirmanak.mealient.extensions.toRecipeIngredientEntity
import gq.kirmanak.mealient.extensions.toRecipeInstructionEntity
import gq.kirmanak.mealient.logging.Logger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeStorageImpl @Inject constructor(
    private val db: AppDb,
    private val logger: Logger,
) : RecipeStorage {
    private val recipeDao: RecipeDao by lazy { db.recipeDao() }

    override suspend fun saveRecipes(recipes: List<RecipeSummaryEntity>) {
        logger.v { "saveRecipes() called with $recipes" }
        db.withTransaction { recipeDao.insertRecipes(recipes) }
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
        db.withTransaction {
            recipeDao.removeAllRecipes()
        }
    }

    override suspend fun saveRecipeInfo(recipe: FullRecipeInfo) {
        logger.v { "saveRecipeInfo() called with: recipe = $recipe" }
        db.withTransaction {
            recipeDao.insertRecipe(recipe.toRecipeEntity())

            recipeDao.deleteRecipeIngredients(recipe.remoteId)
            val ingredients = recipe.recipeIngredients.map {
                it.toRecipeIngredientEntity(recipe.remoteId)
            }
            recipeDao.insertRecipeIngredients(ingredients)

            recipeDao.deleteRecipeInstructions(recipe.remoteId)
            val instructions = recipe.recipeInstructions.map {
                it.toRecipeInstructionEntity(recipe.remoteId)
            }
            recipeDao.insertRecipeInstructions(instructions)
        }
    }

    override suspend fun queryRecipeInfo(recipeId: String): FullRecipeEntity? {
        logger.v { "queryRecipeInfo() called with: recipeId = $recipeId" }
        val fullRecipeInfo = recipeDao.queryFullRecipeInfo(recipeId)
        logger.v { "queryRecipeInfo() returned: $fullRecipeInfo" }
        return fullRecipeInfo
    }
}