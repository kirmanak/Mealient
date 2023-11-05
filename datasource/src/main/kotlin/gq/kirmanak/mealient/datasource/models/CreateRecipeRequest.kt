package gq.kirmanak.mealient.datasource.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateRecipeRequest(
    @SerialName("name") val name: String,
)
