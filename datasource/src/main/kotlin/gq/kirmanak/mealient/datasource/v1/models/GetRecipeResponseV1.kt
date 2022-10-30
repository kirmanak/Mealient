package gq.kirmanak.mealient.datasource.v1.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetRecipeResponseV1(
    @SerialName("id") val remoteId: String,
    @SerialName("name") val name: String,
    @SerialName("recipeYield") val recipeYield: String = "",
    @SerialName("recipeIngredient") val recipeIngredients: List<GetRecipeIngredientResponseV1>,
    @SerialName("recipeInstructions") val recipeInstructions: List<GetRecipeInstructionResponseV1>,
)

@Serializable
data class GetRecipeIngredientResponseV1(
    @SerialName("note") val note: String = "",
)