package gq.kirmanak.mealient.database.shopping_lists.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "shopping_list_item",
    foreignKeys = [
        ForeignKey(
            entity = ShoppingListEntity::class,
            parentColumns = ["shopping_list_id"],
            childColumns = ["shopping_list_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ShoppingListItemEntity(
    @PrimaryKey @ColumnInfo(name = "shopping_list_item_id") val remoteId: String,
    @ColumnInfo(name = "shopping_list_id", index = true) val shoppingListId: String,
    @ColumnInfo(name = "checked") val checked: Boolean,
    @ColumnInfo(name = "position") val position: Int,
    @ColumnInfo(name = "is_food") val isFood: Boolean,
    @ColumnInfo(name = "note") val note: String,
    @ColumnInfo(name = "quantity") val quantity: Double,
    @ColumnInfo(name = "unit") val unit: String,
    @ColumnInfo(name = "food") val food: String,
)
