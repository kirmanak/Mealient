package gq.kirmanak.mealient.datasource.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetFoodLabelResponse(
    @SerialName("name") val name: String,
    @SerialName("color") val color: String,
    @SerialName("groupId") val grpId: String,
    @SerialName("id") val id: String
)