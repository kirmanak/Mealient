package gq.kirmanak.mealient.database.recipe.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "category_recipe",
    primaryKeys = ["category_id", "recipe_id"],
    indices = [Index(value = ["category_id", "recipe_id"], unique = true)],
    foreignKeys = [ForeignKey(
        entity = CategoryEntity::class,
        parentColumns = ["local_id"],
        childColumns = ["category_id"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    ), ForeignKey(
        entity = RecipeSummaryEntity::class,
        parentColumns = ["remote_id"],
        childColumns = ["recipe_id"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class CategoryRecipeEntity(
    @ColumnInfo(name = "category_id") val categoryId: Long,
    @ColumnInfo(name = "recipe_id", index = true) val recipeId: Long
)