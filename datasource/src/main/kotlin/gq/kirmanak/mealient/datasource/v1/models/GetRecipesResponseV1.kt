package gq.kirmanak.mealient.datasource.v1.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetRecipesResponseV1(
    @SerialName("items") val items: List<GetRecipeSummaryResponseV1>,
)