package gq.kirmanak.mealie.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import gq.kirmanak.mealie.data.impl.RoomTypeConverters
import gq.kirmanak.mealie.data.recipes.db.RecipeDao
import gq.kirmanak.mealie.data.recipes.db.entity.*
import javax.inject.Singleton

@Database(
    version = 1,
    entities = [CategoryEntity::class, CategoryRecipeEntity::class, TagEntity::class, TagRecipeEntity::class, RecipeSummaryEntity::class],
    exportSchema = false
)
@TypeConverters(RoomTypeConverters::class)
@Singleton
abstract class MealieDb : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
}