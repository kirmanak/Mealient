package gq.kirmanak.mealient.database.recipe.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

@Entity(tableName = "recipe_summaries")
data class RecipeSummaryEntity(
    @PrimaryKey @ColumnInfo(name = "recipe_id") val remoteId: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "slug") val slug: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "date_added") val dateAdded: LocalDate,
    @ColumnInfo(name = "date_updated") val dateUpdated: LocalDateTime,
    @ColumnInfo(name = "image_id") val imageId: String?,
    @ColumnInfo(name = "is_favorite", defaultValue = "false") val isFavorite: Boolean,
)