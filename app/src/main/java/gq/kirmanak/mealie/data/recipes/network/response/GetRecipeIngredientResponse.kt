package gq.kirmanak.mealie.data.recipes.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetRecipeIngredientResponse(
    @SerialName("title") val title: String = "",
    @SerialName("note") val note: String = "",
    @SerialName("unit") val unit: String = "",
    @SerialName("food") val food: String = "",
    @SerialName("disableAmount") val disableAmount: Boolean,
    @SerialName("quantity") val quantity: Int,
)
