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
    @ColumnInfo(name = "shopping_list_item_checked") val checked: Boolean,
    @ColumnInfo(name = "shopping_list_item_position") val position: Int,
    @ColumnInfo(name = "shopping_list_item_is_food") val isFood: Boolean,
    @ColumnInfo(name = "shopping_list_item_note") val note: String,
    @ColumnInfo(name = "shopping_list_item_quantity") val quantity: Double,
    @ColumnInfo(name = "shopping_list_item_unit") val unit: String,
    @ColumnInfo(name = "shopping_list_item_food") val food: String,
)
