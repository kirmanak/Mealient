package gq.kirmanak.mealient.database.recipe.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe")
data class RecipeEntity(
    @PrimaryKey @ColumnInfo(name = "recipe_id") val remoteId: String,
    @ColumnInfo(name = "recipe_yield") val recipeYield: String,
    @ColumnInfo(name = "recipe_disable_amounts", defaultValue = "true") val disableAmounts: Boolean,
)
