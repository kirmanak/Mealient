package gq.kirmanak.mealient.datasource.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetRecipeIngredientUnitResponse(
    @SerialName("name") val name: String = "",
    @SerialName("id") val id: String = "",
)