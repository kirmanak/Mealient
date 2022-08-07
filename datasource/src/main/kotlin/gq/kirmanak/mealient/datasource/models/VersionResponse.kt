package gq.kirmanak.mealient.datasource.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VersionResponse(
    @SerialName("production")
    val production: Boolean,
    @SerialName("version")
    val version: String,
    @SerialName("demoStatus")
    val demoStatus: Boolean,
)