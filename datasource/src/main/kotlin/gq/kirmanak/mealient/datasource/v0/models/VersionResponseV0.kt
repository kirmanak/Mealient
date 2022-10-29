package gq.kirmanak.mealient.datasource.v0.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VersionResponseV0(
    @SerialName("production") val production: Boolean,
    @SerialName("version") val version: String,
    @SerialName("demoStatus") val demoStatus: Boolean,
)