package gq.kirmanak.mealient.datasource.v1.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateRecipeRequestV1(
    @SerialName("description") val description: String,
    @SerialName("recipeYield") val recipeYield: String,
    @SerialName("recipeIngredient") val recipeIngredient: List<AddRecipeIngredientV1>,
    @SerialName("recipeInstructions") val recipeInstructions: List<AddRecipeInstructionV1>,
    @SerialName("settings") val settings: AddRecipeSettingsV1,
)

@Serializable
data class AddRecipeIngredientV1(
    @SerialName("referenceId") val id: String,
    @SerialName("note") val note: String,
)

@Serializable
data class AddRecipeInstructionV1(
    @SerialName("id") val id: String,
    @SerialName("text") val text: String = "",
    @SerialName("ingredientReferences") val ingredientReferences: List<String>,
)

@Serializable
data class AddRecipeSettingsV1(
    @SerialName("disableComments") val disableComments: Boolean,
    @SerialName("public") val public: Boolean,
)