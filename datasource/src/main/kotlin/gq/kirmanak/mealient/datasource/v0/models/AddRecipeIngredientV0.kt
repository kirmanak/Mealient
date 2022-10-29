package gq.kirmanak.mealient.datasource.v0.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddRecipeIngredientV0(
    @SerialName("disableAmount") val disableAmount: Boolean = true,
    @SerialName("food") val food: String? = null,
    @SerialName("note") val note: String = "",
    @SerialName("quantity") val quantity: Int = 1,
    @SerialName("title") val title: String? = null,
    @SerialName("unit") val unit: String? = null,
)