package gq.kirmanak.mealient.datasource.v1.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetUserInfoResponseV1(
    @SerialName("id") val id: String,
    @SerialName("favoriteRecipes") val favoriteRecipes: List<String> = emptyList(),
)
