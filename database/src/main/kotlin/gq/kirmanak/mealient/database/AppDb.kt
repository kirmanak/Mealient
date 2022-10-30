package gq.kirmanak.mealient.database

import androidx.room.*
import androidx.room.migration.AutoMigrationSpec
import gq.kirmanak.mealient.database.recipe.RecipeDao
import gq.kirmanak.mealient.database.recipe.entity.*

@Database(
    version = 5,
    entities = [
        CategoryEntity::class,
        CategoryRecipeEntity::class,
        TagEntity::class,
        TagRecipeEntity::class,
        RecipeSummaryEntity::class,
        RecipeEntity::class,
        RecipeIngredientEntity::class,
        RecipeInstructionEntity::class
    ],
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5, spec = AppDb.From4To5Migration::class),
    ]
)
@TypeConverters(RoomTypeConverters::class)
abstract class AppDb : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao

    @DeleteColumn(tableName = "recipe_instruction", columnName = "title")
    @DeleteColumn(tableName = "recipe_ingredient", columnName = "title")
    @DeleteColumn(tableName = "recipe_ingredient", columnName = "unit")
    @DeleteColumn(tableName = "recipe_ingredient", columnName = "food")
    @DeleteColumn(tableName = "recipe_ingredient", columnName = "disable_amount")
    @DeleteColumn(tableName = "recipe_ingredient", columnName = "quantity")
    class From4To5Migration : AutoMigrationSpec
}