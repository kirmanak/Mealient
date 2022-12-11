package gq.kirmanak.mealient.datasource.v0.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateApiTokenRequestV0(
    @SerialName("name") val name: String,
)
