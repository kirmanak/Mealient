package gq.kirmanak.mealient.datasource.v0.models

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetRecipeResponseV0(
    @SerialName("id") val remoteId: Int,
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
    @SerialName("recipeIngredient") val recipeIngredients: List<GetRecipeIngredientResponseV0>,
    @SerialName("recipeInstructions") val recipeInstructions: List<GetRecipeInstructionResponseV0>,
)
