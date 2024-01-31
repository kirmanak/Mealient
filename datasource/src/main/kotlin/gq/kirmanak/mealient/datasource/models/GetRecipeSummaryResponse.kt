package gq.kirmanak.mealient.datasource.models

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetRecipeSummaryResponse(
    @SerialName("id") val remoteId: String,
    @SerialName("name") val name: String,
    @SerialName("slug") val slug: String,
    @SerialName("description") val description: String = "",
    @SerialName("recipeYield") val recipeYield: String = "",
    @SerialName("dateAdded") val dateAdded: LocalDate,
    @SerialName("dateUpdated") val dateUpdated: LocalDateTime,
    @SerialName("rating") val rating: Long?,
    @SerialName("userId") val userId: String,
    @SerialName("groupId") val groupId: String,
    @SerialName("totalTime") val totalTime: String = "",
    @SerialName("prepTime") val prepTime: String = "",
    @SerialName("cookTime") val cookTime: String = "",
    @SerialName("performTime") val performTime: String = "",
)