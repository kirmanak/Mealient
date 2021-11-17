package gq.kirmanak.mealie.data.recipes.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

@Entity(tableName = "recipe_summaries", indices = [Index(value = ["remote_id"], unique = true)])
data class RecipeSummaryEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "local_id") val localId: Long = 0,
    @ColumnInfo(name = "remote_id") val remoteId: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "slug") val slug: String,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "rating") val rating: Int?,
    @ColumnInfo(name = "date_added") val dateAdded: LocalDate,
    @ColumnInfo(name = "date_updated") val dateUpdated: LocalDateTime
) {
    override fun toString(): String {
        return "RecipeEntity(localId=$localId, remoteId=$remoteId, name='$name')"
    }
}