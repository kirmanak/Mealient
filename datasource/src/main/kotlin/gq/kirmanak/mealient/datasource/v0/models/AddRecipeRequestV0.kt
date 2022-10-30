package gq.kirmanak.mealient.datasource.v0.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddRecipeRequestV0(
    @SerialName("name") val name: String = "",
    @SerialName("description") val description: String = "",
    @SerialName("recipeYield") val recipeYield: String = "",
    @SerialName("recipeIngredient") val recipeIngredient: List<AddRecipeIngredientV0> = emptyList(),
    @SerialName("recipeInstructions") val recipeInstructions: List<AddRecipeInstructionV0> = emptyList(),
    @SerialName("settings") val settings: AddRecipeSettingsV0 = AddRecipeSettingsV0(),
)

@Serializable
data class AddRecipeIngredientV0(
    @SerialName("note") val note: String = "",
)

@Serializable
data class AddRecipeInstructionV0(
    @SerialName("text") val text: String = "",
)

@Serializable
data class AddRecipeSettingsV0(
    @SerialName("disableComments") val disableComments: Boolean = false,
    @SerialName("public") val public: Boolean = true,
)