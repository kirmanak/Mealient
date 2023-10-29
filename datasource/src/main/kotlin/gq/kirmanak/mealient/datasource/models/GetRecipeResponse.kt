package gq.kirmanak.mealient.datasource.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetRecipeResponse(
    @SerialName("id") val remoteId: String,
    @SerialName("name") val name: String,
    @SerialName("recipeYield") val recipeYield: String = "",
    @SerialName("recipeIngredient") val recipeIngredients: List<GetRecipeIngredientResponse> = emptyList(),
    @SerialName("recipeInstructions") val recipeInstructions: List<GetRecipeInstructionResponse> = emptyList(),
    @SerialName("settings") val settings: GetRecipeSettingsResponse? = null,
)

@Serializable
data class GetRecipeSettingsResponse(
    @SerialName("disableAmount") val disableAmount: Boolean,
)

@Serializable
data class GetRecipeIngredientResponse(
    @SerialName("note") val note: String = "",
    @SerialName("unit") val unit: GetRecipeIngredientUnitResponse?,
    @SerialName("food") val food: GetRecipeIngredientFoodResponse?,
    @SerialName("quantity") val quantity: Double?,
    @SerialName("title") val title: String?,
)

@Serializable
data class GetRecipeInstructionResponse(
    @SerialName("text") val text: String,
)