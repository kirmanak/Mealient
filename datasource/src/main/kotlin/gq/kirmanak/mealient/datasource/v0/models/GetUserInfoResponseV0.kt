package gq.kirmanak.mealient.datasource.v0.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetUserInfoResponseV0(
    @SerialName("favoriteRecipes") val favoriteRecipes: List<String> = emptyList(),
)
