package gq.kirmanak.mealient.datasource.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetUserInfoResponse(
    @SerialName("id") val id: String,
    @SerialName("favoriteRecipes") val favoriteRecipes: List<String> = emptyList(),
)
