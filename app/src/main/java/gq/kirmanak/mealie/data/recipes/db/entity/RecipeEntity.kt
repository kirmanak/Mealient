package gq.kirmanak.mealie.data.recipes.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe")
data class RecipeEntity(
    @PrimaryKey @ColumnInfo(name = "remote_id") val remoteId: Long,
    @ColumnInfo(name = "recipe_yield") val recipeYield: String,
)
