package gq.kirmanak.mealie.data.recipes.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetRecipeInstructionResponse(
    @SerialName("title") val title: String = "",
    @SerialName("text") val text: String,
)
