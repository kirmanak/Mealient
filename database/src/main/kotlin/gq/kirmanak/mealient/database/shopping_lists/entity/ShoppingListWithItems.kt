package gq.kirmanak.mealient.database.shopping_lists.entity

import androidx.room.Embedded
import androidx.room.Relation
import gq.kirmanak.mealient.database.recipe.entity.RecipeEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeIngredientEntity

data class ShoppingListWithItems(
    @Embedded val shoppingList: ShoppingListEntity,
    @Relation(
        parentColumn = "shopping_list_id",
        entityColumn = "shopping_list_id",
        entity = ShoppingListItemEntity::class
    )
    val shoppingListItems: List<ShoppingListItemWithRecipes>
)

data class ShoppingListItemWithRecipes(
    @Embedded val item: ShoppingListItemEntity,
    @Relation(
        parentColumn = "shopping_list_item_id",
        entityColumn = "shopping_list_item_id",
        entity = ShoppingListItemRecipeReferenceEntity::class,
    )
    val recipes: List<ItemRecipeReferenceWithRecipe>,
)

data class ItemRecipeReferenceWithRecipe(
    @Embedded val reference: ShoppingListItemRecipeReferenceEntity,
    @Relation(
        parentColumn = "recipe_id",
        entityColumn = "recipe_id",
        entity = RecipeEntity::class,
    )
    val recipe: RecipeWithIngredients,
)

data class RecipeWithIngredients(
    @Embedded val recipe: RecipeEntity,
    @Relation(
        parentColumn = "recipe_id",
        entityColumn = "recipe_id"
    )
    val ingredients: List<RecipeIngredientEntity>
)