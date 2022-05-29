package gq.kirmanak.mealient.data.recipes.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "tag_recipe",
    primaryKeys = ["tag_id", "recipe_id"],
    foreignKeys = [ForeignKey(
        entity = TagEntity::class,
        parentColumns = ["local_id"],
        childColumns = ["tag_id"],
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
data class TagRecipeEntity(
    @ColumnInfo(name = "tag_id") val tagId: Long,
    @ColumnInfo(name = "recipe_id", index = true) val recipeId: Long
)