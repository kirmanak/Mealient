package gq.kirmanak.mealient.datasource.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetRecipeResponse(
    @SerialName("id") val remoteId: String,
    @SerialName("name") val name: String,
    @SerialName("recipeYield") val recipeYield: String = "",
    @SerialName("recipeIngredient") val ingredients: List<GetRecipeIngredientResponse> = emptyList(),
    @SerialName("recipeInstructions") val instructions: List<GetRecipeInstructionResponse> = emptyList(),
    @SerialName("settings") val settings: GetRecipeSettingsResponse? = null,
)

@Serializable
data class GetRecipeSettingsResponse(
    @SerialName("disableAmount") val disableAmount: Boolean,
)

@Serializable
data class GetRecipeIngredientResponse(
    @SerialName("note") val note: String = "",
    @SerialName("unit") val unit: GetUnitResponse?,
    @SerialName("food") val food: GetFoodResponse?,
    @SerialName("quantity") val quantity: Double?,
    @SerialName("display") val display: String?,
    @SerialName("referenceId") val referenceId: String,
)

@Serializable
data class GetRecipeInstructionResponse(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String = "",
    @SerialName("text") val text: String,
    @SerialName("ingredientReferences") val ingredientReferences: List<GetRecipeInstructionIngredientReference> = emptyList(),
)

@Serializable
data class GetRecipeInstructionIngredientReference(
    @SerialName("referenceId") val referenceId: String,
)