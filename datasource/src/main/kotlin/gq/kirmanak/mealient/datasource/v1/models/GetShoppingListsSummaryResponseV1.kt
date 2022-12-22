package gq.kirmanak.mealient.datasource.v1.models

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetShoppingListsSummaryResponseV1(
    @SerialName("id") val id: String,
    @SerialName("groupId") val groupId: String,
    @SerialName("createdAt") val createdAt: Instant?,
    @SerialName("updateAt") val updateAt: Instant?,
    @SerialName("name") val name: String?,
)