package gq.kirmanak.mealient.datasource.v1.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetShoppingListsSummaryResponseV1(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String?,
)