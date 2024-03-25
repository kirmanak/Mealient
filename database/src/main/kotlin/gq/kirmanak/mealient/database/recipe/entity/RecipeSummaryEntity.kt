package gq.kirmanak.mealient.database.recipe.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

@Entity(tableName = "recipe_summaries")
data class RecipeSummaryEntity(
    @PrimaryKey @ColumnInfo(name = "recipe_id") val remoteId: String,
    @ColumnInfo(name = "recipe_summaries_name") val name: String,
    @ColumnInfo(name = "recipe_summaries_slug") val slug: String,
    @ColumnInfo(name = "recipe_summaries_description") val description: String,
    @ColumnInfo(name = "recipe_summaries_yield") val recipeYield: String,
    @ColumnInfo(name = "recipe_summaries_date_added") val dateAdded: LocalDate,
    @ColumnInfo(name = "recipe_summaries_date_updated") val dateUpdated: LocalDateTime,
    @ColumnInfo(name = "recipe_summaries_image_id") val imageId: String?,
    @ColumnInfo(name = "recipe_summaries_is_favorite") val isFavorite: Boolean,
    @ColumnInfo(name = "recipe_summaries_rating") val rating: Long,
    @ColumnInfo(name = "recipe_summaries_userId") val userId: String,
    @ColumnInfo(name = "recipe_summaries_groupId") val groupId: String,
    @ColumnInfo(name = "recipe_summaries_totalTime") val totalTime: String = "",
    @ColumnInfo(name = "recipe_summaries_prepTime") val prepTime: String = "",
    @ColumnInfo(name = "recipe_summaries_cookTime") val cookTime: String = "",
    @ColumnInfo(name = "recipe_summaries_performTime") val performTime: String = "",
)