package gq.kirmanak.mealient.datasource.v1.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetRecipeInstructionResponseV1(
    @SerialName("title") val title: String = "",
    @SerialName("text") val text: String,
)
