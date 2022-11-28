package gq.kirmanak.mealient.datasource.v0.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParseRecipeURLRequestV0(
    @SerialName("url") val url: String,
)
