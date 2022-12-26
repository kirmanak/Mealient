package gq.kirmanak.mealient.database.shopping_lists.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_lists")
data class ShoppingListEntity(
    @PrimaryKey @ColumnInfo(name = "shopping_list_id") val remoteId: String,
    @ColumnInfo(name = "name") val name: String,
)
