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
    @PrimaryKey @ColumnInfo(name = "recipe_ingredient_id") val id: String,
    @ColumnInfo(name = "recipe_id", index = true) val recipeId: String,
    @ColumnInfo(name = "recipe_ingredient_note") val note: String,
    @ColumnInfo(name = "recipe_ingredient_food") val food: String?,
    @ColumnInfo(name = "recipe_ingredient_unit") val unit: String?,
    @ColumnInfo(name = "recipe_ingredient_quantity") val quantity: Double?,
    @ColumnInfo(name = "recipe_ingredient_display") val display: String?,
)