package gq.kirmanak.mealient.database

import androidx.room.*
import gq.kirmanak.mealient.database.recipe.RecipeDao
import gq.kirmanak.mealient.database.recipe.entity.*
import gq.kirmanak.mealient.database.shopping_lists.ShoppingListsDao
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListEntity
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListItemEntity
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListItemRecipeReferenceEntity

@Database(
    version = 9,
    entities = [
        RecipeSummaryEntity::class,
        RecipeEntity::class,
        RecipeIngredientEntity::class,
        RecipeInstructionEntity::class,
        ShoppingListEntity::class,
        ShoppingListItemEntity::class,
        ShoppingListItemRecipeReferenceEntity::class,
    ]
)
@TypeConverters(RoomTypeConverters::class)
internal abstract class AppDb : RoomDatabase() {

    abstract fun recipeDao(): RecipeDao

    abstract fun shoppingListsDao(): ShoppingListsDao
}