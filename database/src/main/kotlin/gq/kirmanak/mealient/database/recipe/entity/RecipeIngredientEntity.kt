package gq.kirmanak.mealient.database.recipe.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe_ingredient")
data class RecipeIngredientEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "local_id") val localId: Long = 0,
    @ColumnInfo(name = "recipe_id") val recipeId: String,
    @ColumnInfo(name = "note") val note: String,
)