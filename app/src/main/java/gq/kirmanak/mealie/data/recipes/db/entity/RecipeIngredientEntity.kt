package gq.kirmanak.mealie.data.recipes.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe_ingredient")
data class RecipeIngredientEntity(
    @PrimaryKey @ColumnInfo(name = "recipe_id") val recipeId: Long,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "note") val note: String,
    @ColumnInfo(name = "unit") val unit: String,
    @ColumnInfo(name = "food") val food: String,
    @ColumnInfo(name = "disable_amount") val disableAmount: Boolean,
    @ColumnInfo(name = "quantity") val quantity: Int,
)