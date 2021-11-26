package gq.kirmanak.mealient.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import gq.kirmanak.mealient.data.impl.util.RoomTypeConverters
import gq.kirmanak.mealient.data.recipes.db.RecipeDao
import gq.kirmanak.mealient.data.recipes.db.entity.*

@Database(
    version = 1,
    entities = [CategoryEntity::class, CategoryRecipeEntity::class, TagEntity::class, TagRecipeEntity::class, RecipeSummaryEntity::class, RecipeEntity::class, RecipeIngredientEntity::class, RecipeInstructionEntity::class],
    exportSchema = false
)
@TypeConverters(RoomTypeConverters::class)
abstract class AppDb : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
}