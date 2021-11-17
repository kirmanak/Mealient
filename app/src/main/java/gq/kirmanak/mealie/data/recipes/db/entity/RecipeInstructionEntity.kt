package gq.kirmanak.mealie.data.recipes.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe_instruction")
data class RecipeInstructionEntity(
    @PrimaryKey @ColumnInfo(name = "recipe_id") val recipeId: Long,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "text") val text: String,
)
