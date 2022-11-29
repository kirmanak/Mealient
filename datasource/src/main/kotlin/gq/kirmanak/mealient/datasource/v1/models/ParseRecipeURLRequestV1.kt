package gq.kirmanak.mealient.datasource.v1.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParseRecipeURLRequestV1(
    @SerialName("url") val url: String,
    @SerialName("includeTags") val includeTags: Boolean
)
