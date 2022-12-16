package gq.kirmanak.mealient.datasource.v0.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateApiTokenResponseV0(
    @SerialName("token") val token: String
)
