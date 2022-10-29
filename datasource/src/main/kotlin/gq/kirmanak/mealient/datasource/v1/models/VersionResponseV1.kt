package gq.kirmanak.mealient.datasource.v1.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VersionResponseV1(
    @SerialName("production") val production: Boolean,
    @SerialName("version") val version: String,
    @SerialName("demoStatus") val demoStatus: Boolean,
    @SerialName("allowSignup") val allowSignup: Boolean,
)