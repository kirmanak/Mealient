package gq.kirmanak.mealient.datasource.v1.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetRecipeIngredientUnitResponseV1(
    @SerialName("name") val name: String = "",
    @SerialName("id") val id: String = "",
)