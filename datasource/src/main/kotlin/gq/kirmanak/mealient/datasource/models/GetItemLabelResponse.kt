package gq.kirmanak.mealient.datasource.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetItemLabelResponse(
    @SerialName("name") val name: String,
    @SerialName("color") val color: String,
    @SerialName("groupId") val grpId: String,
    @SerialName("id") val id: String
)