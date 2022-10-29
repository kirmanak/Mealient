package gq.kirmanak.mealient.datasource.v1.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetRecipeIngredientResponseV1(
    @SerialName("title") val title: String = "",
    @SerialName("note") val note: String = "",
    @SerialName("unit") val unit: String = "",
    @SerialName("food") val food: String = "",
    @SerialName("disableAmount") val disableAmount: Boolean,
    @SerialName("quantity") val quantity: Double,
)
