package gq.kirmanak.mealient.database.recipe.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe_ingredient")
data class RecipeIngredientEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "local_id") val localId: Long = 0,
    @ColumnInfo(name = "recipe_id") val recipeId: String,
    @ColumnInfo(name = "note") val note: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RecipeIngredientEntity

        if (recipeId != other.recipeId) return false
        if (note != other.note) return false

        return true
    }

    override fun hashCode(): Int {
        var result = recipeId.hashCode()
        result = 31 * result + note.hashCode()
        return result
    }
}