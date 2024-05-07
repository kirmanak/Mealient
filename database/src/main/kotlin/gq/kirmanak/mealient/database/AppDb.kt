package gq.kirmanak.mealient.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import gq.kirmanak.mealient.database.recipe.RecipeDao
import gq.kirmanak.mealient.database.recipe.entity.RecipeEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeIngredientEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeIngredientToInstructionEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeInstructionEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeSettingsEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity

@Database(
    version = 13,
    entities = [
        RecipeSummaryEntity::class,
        RecipeEntity::class,
        RecipeSettingsEntity::class,
        RecipeIngredientEntity::class,
        RecipeInstructionEntity::class,
        RecipeIngredientToInstructionEntity::class,
    ]
)
@TypeConverters(RoomTypeConverters::class)
internal abstract class AppDb : RoomDatabase() {

    abstract fun recipeDao(): RecipeDao
}