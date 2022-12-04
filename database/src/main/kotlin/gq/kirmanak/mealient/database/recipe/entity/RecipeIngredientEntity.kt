package gq.kirmanak.mealient.database.recipe.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe_ingredient")
data class RecipeIngredientEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "local_id") val localId: Long = 0,
    @ColumnInfo(name = "recipe_id") val recipeId: String,
    @ColumnInfo(name = "note") val note: String,
    @ColumnInfo(name = "food") val food: String?,
    @ColumnInfo(name = "unit") val unit: String?,
    @ColumnInfo(name = "quantity") val quantity: Double?,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RecipeIngredientEntity

        if (recipeId != other.recipeId) return false
        if (note != other.note) return false
        if (food != other.food) return false
        if (unit != other.unit) return false
        if (quantity != other.quantity) return false

        return true
    }

    override fun hashCode(): Int {
        var result = recipeId.hashCode()
        result = 31 * result + note.hashCode()
        result = 31 * result + (food?.hashCode() ?: 0)
        result = 31 * result + (unit?.hashCode() ?: 0)
        result = 31 * result + quantity.hashCode()
        return result
    }
}