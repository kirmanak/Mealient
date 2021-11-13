package gq.kirmanak.mealie.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import gq.kirmanak.mealie.data.impl.RoomTypeConverters
import gq.kirmanak.mealie.data.recipes.db.*
import javax.inject.Singleton

@Database(
    version = 1,
    entities = [CategoryEntity::class, CategoryRecipeEntity::class, TagEntity::class, TagRecipeEntity::class, RecipeEntity::class],
    exportSchema = false
)
@TypeConverters(RoomTypeConverters::class)
@Singleton
abstract class MealieDb : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
}