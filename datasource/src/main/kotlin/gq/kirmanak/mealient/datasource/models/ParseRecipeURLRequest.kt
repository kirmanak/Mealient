package gq.kirmanak.mealient.datasource.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParseRecipeURLRequest(
    @SerialName("url") val url: String,
    @SerialName("includeTags") val includeTags: Boolean
)
