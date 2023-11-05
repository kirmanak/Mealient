package gq.kirmanak.mealient.datasource.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetFoodsResponse(
    @SerialName("items") val items: List<GetFoodResponse>,
)

@Serializable
data class GetFoodResponse(
    @SerialName("name") val name: String,
    @SerialName("id") val id: String,
)
