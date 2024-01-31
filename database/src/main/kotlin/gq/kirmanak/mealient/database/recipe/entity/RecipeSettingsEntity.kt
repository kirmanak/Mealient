package gq.kirmanak.mealient.database.recipe.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe_settings")
data class RecipeSettingsEntity(
    @PrimaryKey @ColumnInfo(name = "recipe_id") val remoteId: String,
    @ColumnInfo(name = "public") val public: Boolean,
    @ColumnInfo(name = "show_nutrition") val showNutrition: Boolean,
    @ColumnInfo(name = "show_assets") val showAssets: Boolean,
    @ColumnInfo(name = "landscape_view") val landscapeView: Boolean,
    @ColumnInfo(name = "disable_comments") val disableComments: Boolean,
    @ColumnInfo(name = "disable_amounts", defaultValue = "true") val disableAmounts: Boolean,
    @ColumnInfo(name = "locked") val locked: Boolean,
)
