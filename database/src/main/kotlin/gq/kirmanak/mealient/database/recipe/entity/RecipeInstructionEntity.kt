package gq.kirmanak.mealient.database.recipe.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe_instruction")
data class RecipeInstructionEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "local_id") val localId: Long = 0,
    @ColumnInfo(name = "recipe_id") val recipeId: Long,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "text") val text: String,
)
