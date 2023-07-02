package gq.kirmanak.mealient.database.shopping_lists

import gq.kirmanak.mealient.database.recipe.entity.RecipeEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeIngredientEntity
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListItemEntity
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListItemRecipeReferenceEntity
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListWithItems
import kotlinx.coroutines.flow.Flow

interface ShoppingListsStorage {

    suspend fun saveShoppingListItems(
        shoppingListItems: List<ShoppingListItemEntity>,
        recipeReferences: List<ShoppingListItemRecipeReferenceEntity>,
        recipes: List<RecipeEntity>,
        recipeIngredients: List<RecipeIngredientEntity>,
    )

    fun getShoppingListWithItems(id: String): Flow<ShoppingListWithItems>

    suspend fun clearLocalData()
}