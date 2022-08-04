package gq.kirmanak.mealient.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import gq.kirmanak.mealient.database.recipe.RecipeDao
import gq.kirmanak.mealient.database.recipe.entity.*

@Database(
    version = 2,
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
        AutoMigration(from = 1, to = 2)
    ]
)
@TypeConverters(RoomTypeConverters::class)
abstract class AppDb : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
}