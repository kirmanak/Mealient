package gq.kirmanak.mealient.datasource.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetShoppingListsSummaryResponse(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String?,
)