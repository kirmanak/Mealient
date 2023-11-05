package gq.kirmanak.mealient.datasource.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetShoppingListsResponse(
    @SerialName("page") val page: Int,
    @SerialName("per_page") val perPage: Int,
    @SerialName("total") val total: Int,
    @SerialName("total_pages") val totalPages: Int,
    @SerialName("items") val items: List<GetShoppingListsSummaryResponse>,
)
