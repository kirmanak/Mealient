package gq.kirmanak.mealient.datasource.v1.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetUnitsResponseV1(
    @SerialName("items") val items: List<GetUnitResponseV1>
)

@Serializable
data class GetUnitResponseV1(
    @SerialName("name") val name: String,
    @SerialName("id") val id: String
)
