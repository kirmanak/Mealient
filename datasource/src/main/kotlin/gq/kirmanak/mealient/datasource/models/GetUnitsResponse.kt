package gq.kirmanak.mealient.datasource.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetUnitsResponse(
    @SerialName("items") val items: List<GetUnitResponse>
)

@Serializable
data class GetUnitResponse(
    @SerialName("name") val name: String,
    @SerialName("pluralName") val pluralName: String? = null,
    @SerialName("id") val id: String
)
