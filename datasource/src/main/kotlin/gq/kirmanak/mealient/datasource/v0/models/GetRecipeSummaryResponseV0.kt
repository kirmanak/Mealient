package gq.kirmanak.mealient.datasource.v0.models

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetRecipeSummaryResponseV0(
    @SerialName("id") val remoteId: Int,
    @SerialName("name") val name: String,
    @SerialName("slug") val slug: String,
    @SerialName("description") val description: String = "",
    @SerialName("dateAdded") val dateAdded: LocalDate,
    @SerialName("dateUpdated") val dateUpdated: LocalDateTime
)