package gq.kirmanak.mealient.datasource.v1.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetRecipeResponseV1(
    @SerialName("id") val remoteId: String,
    @SerialName("name") val name: String,
    @SerialName("recipeYield") val recipeYield: String = "",
    @SerialName("recipeIngredient") val recipeIngredients: List<GetRecipeIngredientResponseV1> = emptyList(),
    @SerialName("recipeInstructions") val recipeInstructions: List<GetRecipeInstructionResponseV1> = emptyList(),
    @SerialName("settings") val settings: GetRecipeSettingsResponseV1,
)

@Serializable
data class GetRecipeSettingsResponseV1(
    @SerialName("disableAmount") val disableAmount: Boolean,
)

@Serializable
data class GetRecipeIngredientResponseV1(
    @SerialName("note") val note: String = "",
    @SerialName("unit") val unit: GetRecipeIngredientUnitResponseV1?,
    @SerialName("food") val food: GetRecipeIngredientFoodResponseV1?,
    @SerialName("quantity") val quantity: Double?,
)

@Serializable
data class GetRecipeIngredientFoodResponseV1(
    @SerialName("name") val name: String = "",
)

@Serializable
data class GetRecipeIngredientUnitResponseV1(
    @SerialName("name") val name: String = "",
)

@Serializable
data class GetRecipeInstructionResponseV1(
    @SerialName("text") val text: String,
)