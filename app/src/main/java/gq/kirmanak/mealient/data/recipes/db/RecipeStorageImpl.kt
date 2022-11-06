package gq.kirmanak.mealient.data.recipes.db

import androidx.paging.PagingSource
import androidx.room.withTransaction
import gq.kirmanak.mealient.data.recipes.network.FullRecipeInfo
import gq.kirmanak.mealient.data.recipes.network.RecipeSummaryInfo
import gq.kirmanak.mealient.database.AppDb
import gq.kirmanak.mealient.database.recipe.RecipeDao
import gq.kirmanak.mealient.database.recipe.entity.FullRecipeEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.extensions.toRecipeEntity
import gq.kirmanak.mealient.extensions.toRecipeIngredientEntity
import gq.kirmanak.mealient.extensions.toRecipeInstructionEntity
import gq.kirmanak.mealient.extensions.toRecipeSummaryEntity
import gq.kirmanak.mealient.logging.Logger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeStorageImpl @Inject constructor(
    private val db: AppDb,
    private val logger: Logger,
) : RecipeStorage {
    private val recipeDao: RecipeDao by lazy { db.recipeDao() }

    override suspend fun saveRecipes(
        recipes: List<RecipeSummaryInfo>
    ) = db.withTransaction {
        logger.v { "saveRecipes() called with $recipes" }

        for (recipe in recipes) {
            val recipeSummaryEntity = recipe.toRecipeSummaryEntity()
            recipeDao.insertRecipe(recipeSummaryEntity)
        }
    }


    override fun queryRecipes(): PagingSource<Int, RecipeSummaryEntity> {
        logger.v { "queryRecipes() called" }
        return recipeDao.queryRecipesByPages()
    }

    override suspend fun refreshAll(recipes: List<RecipeSummaryInfo>) {
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