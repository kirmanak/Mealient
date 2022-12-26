package gq.kirmanak.mealient.database.shopping_lists.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import gq.kirmanak.mealient.database.recipe.entity.RecipeEntity

@Entity(
    tableName = "shopping_list_item_recipe_reference",
    foreignKeys = [
        ForeignKey(
            entity = ShoppingListItemEntity::class,
            parentColumns = ["remote_id"],
            childColumns = ["shopping_list_item_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = RecipeEntity::class,
            parentColumns = ["remote_id"],
            childColumns = ["recipe_id"],
            onDelete = ForeignKey.CASCADE
        ),
    ],
    primaryKeys = ["shopping_list_item_id", "recipe_id"]
)
data class ShoppingListItemRecipeReferenceEntity(
    @ColumnInfo(name = "shopping_list_item_id", index = true) val shoppingListItemId: String,
    @ColumnInfo(name = "recipe_id", index = true) val recipeId: String,
    @ColumnInfo(name = "quantity") val quantity: Double,
)
