package gq.kirmanak.mealient.database.recipe.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4


@Entity(tableName = "recipe_summaries_fts")
@Fts4(contentEntity = RecipeSummaryEntity::class)
data class RecipeSummaryFtsEntity(
    @ColumnInfo(name = "remote_id") val remoteId: String,
    @ColumnInfo(name = "name") val name: String,
)
