package gq.kirmanak.mealient.datasource.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateShoppingListItemRequest(
    @SerialName("shopping_list_id") val shoppingListId: String,
    @SerialName("checked") val checked: Boolean,
    @SerialName("position") val position: Int?,
    @SerialName("is_food") val isFood: Boolean,
    @SerialName("note") val note: String,
    @SerialName("quantity") val quantity: Double,
    @SerialName("food_id") val foodId: String?,
    @SerialName("unit_id") val unitId: String?,
)
