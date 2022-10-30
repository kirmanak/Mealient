package gq.kirmanak.mealient.datasource.v0.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetRecipeResponseV0(
    @SerialName("id") val remoteId: Int,
    @SerialName("name") val name: String,
    @SerialName("recipeYield") val recipeYield: String = "",
    @SerialName("recipeIngredient") val recipeIngredients: List<GetRecipeIngredientResponseV0>,
    @SerialName("recipeInstructions") val recipeInstructions: List<GetRecipeInstructionResponseV0>,
)
