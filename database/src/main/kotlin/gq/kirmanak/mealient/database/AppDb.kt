package gq.kirmanak.mealient.database

import androidx.room.*
import androidx.room.migration.AutoMigrationSpec
import gq.kirmanak.mealient.database.recipe.RecipeDao
import gq.kirmanak.mealient.database.recipe.entity.*
import gq.kirmanak.mealient.database.shopping_lists.ShoppingListsDao
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListEntity
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListItemEntity
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListItemRecipeReferenceEntity

@Database(
    version = 10,
    entities = [
        RecipeSummaryEntity::class,
        RecipeEntity::class,
        RecipeIngredientEntity::class,
        RecipeInstructionEntity::class,
        ShoppingListEntity::class,
        ShoppingListItemEntity::class,
        ShoppingListItemRecipeReferenceEntity::class,
    ],
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5, spec = AppDb.From4To5Migration::class),
        AutoMigration(from = 5, to = 6, spec = AppDb.From5To6Migration::class),
        AutoMigration(from = 6, to = 7),
        AutoMigration(from = 7, to = 8),
        AutoMigration(from = 8, to = 9),
        AutoMigration(from = 9, to = 10),
    ]
)
@TypeConverters(RoomTypeConverters::class)
internal abstract class AppDb : RoomDatabase() {

    abstract fun recipeDao(): RecipeDao

    abstract fun shoppingListsDao(): ShoppingListsDao

    @DeleteColumn(tableName = "recipe_instruction", columnName = "title")
    @DeleteColumn(tableName = "recipe_ingredient", columnName = "title")
    @DeleteColumn(tableName = "recipe_ingredient", columnName = "unit")
    @DeleteColumn(tableName = "recipe_ingredient", columnName = "food")
    @DeleteColumn(tableName = "recipe_ingredient", columnName = "disable_amount")
    @DeleteColumn(tableName = "recipe_ingredient", columnName = "quantity")
    class From4To5Migration : AutoMigrationSpec

    @DeleteColumn(tableName = "recipe_summaries", columnName = "image")
    @DeleteColumn(tableName = "recipe_summaries", columnName = "rating")
    @DeleteTable(tableName = "tag_recipe")
    @DeleteTable(tableName = "tags")
    @DeleteTable(tableName = "categories")
    @DeleteTable(tableName = "category_recipe")
    class From5To6Migration : AutoMigrationSpec
}