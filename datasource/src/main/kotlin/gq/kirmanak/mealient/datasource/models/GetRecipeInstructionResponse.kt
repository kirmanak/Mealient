package gq.kirmanak.mealient.datasource.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetRecipeInstructionResponse(
    @SerialName("title") val title: String = "",
    @SerialName("text") val text: String,
)
