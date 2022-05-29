package gq.kirmanak.mealient.data.recipes.network.response

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetRecipeSummaryResponse(
    @SerialName("id") val remoteId: Long,
    @SerialName("name") val name: String,
    @SerialName("slug") val slug: String,
    @SerialName("image") val image: String?,
    @SerialName("description") val description: String = "",
    @SerialName("recipeCategory") val recipeCategories: List<String>,
    @SerialName("tags") val tags: List<String>,
    @SerialName("rating") val rating: Int?,
    @SerialName("dateAdded") val dateAdded: LocalDate,
    @SerialName("dateUpdated") val dateUpdated: LocalDateTime
) {
    override fun toString(): String {
        return "GetRecipeSummaryResponse(remoteId=$remoteId, name='$name')"
    }
}