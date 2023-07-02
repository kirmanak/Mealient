package gq.kirmanak.mealient.database.shopping_lists

import androidx.room.withTransaction
import gq.kirmanak.mealient.database.AppDb
import gq.kirmanak.mealient.database.recipe.RecipeDao
import gq.kirmanak.mealient.database.recipe.entity.RecipeEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeIngredientEntity
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListItemEntity
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListItemRecipeReferenceEntity
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListWithItems
import gq.kirmanak.mealient.logging.Logger
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ShoppingListsStorageImpl @Inject constructor(
    private val shoppingListsDao: ShoppingListsDao,
    private val recipeDao: RecipeDao,
    private val appDb: AppDb,
    private val logger: Logger,
) : ShoppingListsStorage {

    override suspend fun saveShoppingListItems(
        shoppingListItems: List<ShoppingListItemEntity>,
        recipeReferences: List<ShoppingListItemRecipeReferenceEntity>,
        recipes: List<RecipeEntity>,
        recipeIngredients: List<RecipeIngredientEntity>,
    ) {
        logger.v { "saveShoppingListItems() called with: shoppingListItems = $shoppingListItems, recipeReferences = $recipeReferences, recipes = $recipes, recipeIngredients = $recipeIngredients" }
        appDb.withTransaction {
            shoppingListsDao.insertShoppingListItems(shoppingListItems)

            recipeDao.insertRecipes(recipes)

            val recipeIds = recipes.map { it.remoteId }.toTypedArray()

            recipeDao.deleteRecipeIngredients(*recipeIds)
            recipeDao.insertRecipeIngredients(recipeIngredients)

            shoppingListsDao.insertShoppingListItemRecipeReferences(recipeReferences)
        }
    }

    override fun getShoppingListWithItems(id: String): Flow<ShoppingListWithItems> {
        logger.v { "getShoppingListWithItems() called with: id = $id" }
        return shoppingListsDao.queryShoppingListWithItems(id)
    }

    override suspend fun clearLocalData() {
        logger.v { "clearLocalData() called" }
        shoppingListsDao.deleteAllShoppingLists()
    }
}