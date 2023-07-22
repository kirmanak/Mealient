package gq.kirmanak.mealient.datasource.v1.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetFoodsResponseV1(
    @SerialName("items") val items: List<GetFoodResponseV1>,
)

@Serializable
data class GetFoodResponseV1(
    @SerialName("name") val name: String,
    @SerialName("id") val id: String,
)
