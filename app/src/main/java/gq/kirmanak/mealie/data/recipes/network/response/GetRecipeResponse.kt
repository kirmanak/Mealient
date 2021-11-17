package gq.kirmanak.mealie.data.recipes.network.response

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetRecipeResponse(
    @SerialName("id") val remoteId: Long,
    @SerialName("name") val name: String,
    @SerialName("slug") val slug: String,
    @SerialName("image") val image: String,
    @SerialName("description") val description: String = "",
    @SerialName("recipeCategory") val recipeCategories: List<String>,
    @SerialName("tags") val tags: List<String>,
    @SerialName("rating") val rating: Int?,
    @SerialName("dateAdded") val dateAdded: LocalDate,
    @SerialName("dateUpdated") val dateUpdated: LocalDateTime,
    @SerialName("recipeYield") val recipeYield: String = "",
    @SerialName("recipeIngredient") val recipeIngredients: List<GetRecipeIngredientResponse>,
    @SerialName("recipeInstructions") val recipeInstructions: List<GetRecipeInstructionResponse>,
)
