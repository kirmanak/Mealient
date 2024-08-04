package gq.kirmanak.mealient.datasource.models

import gq.kirmanak.mealient.datasource.impl.UtcInstantSerializer
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetRecipeSummaryResponse(
    @SerialName("id") val remoteId: String,
    @SerialName("name") val name: String,
    @SerialName("slug") val slug: String,
    @SerialName("description") val description: String = "",
    @SerialName("dateAdded") val dateAdded: LocalDate,
    @Serializable(with = UtcInstantSerializer::class)
    @SerialName("dateUpdated") val dateUpdated: Instant
)