package gq.kirmanak.mealient.database.recipe.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "recipe_ingredient",
    foreignKeys = [
        ForeignKey(
            entity = RecipeEntity::class,
            parentColumns = ["recipe_id"],
            childColumns = ["recipe_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RecipeIngredientEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "recipe_ingredient_local_id") val localId: Long = 0,
    @ColumnInfo(name = "recipe_id", index = true) val recipeId: String,
    @ColumnInfo(name = "recipe_ingredient_note") val note: String,
    @ColumnInfo(name = "recipe_ingredient_food") val food: String?,
    @ColumnInfo(name = "recipe_ingredient_unit") val unit: String?,
    @ColumnInfo(name = "recipe_ingredient_quantity") val quantity: Double?,
    @ColumnInfo(name = "recipe_ingredient_title") val title: String?,
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
        if (title != other.title) return false

        return true
    }

    override fun hashCode(): Int {
        var result = recipeId.hashCode()
        result = 31 * result + note.hashCode()
        result = 31 * result + (food?.hashCode() ?: 0)
        result = 31 * result + (unit?.hashCode() ?: 0)
        result = 31 * result + (quantity?.hashCode() ?: 0)
        result = 31 * result + (title?.hashCode() ?: 0)
        return result
    }
}