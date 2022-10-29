package gq.kirmanak.mealient.datasource.v0.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddRecipeRequestV0(
    @SerialName("name") val name: String = "",
    @SerialName("description") val description: String = "",
    @SerialName("image") val image: String = "",
    @SerialName("recipeYield") val recipeYield: String = "",
    @SerialName("recipeIngredient") val recipeIngredient: List<AddRecipeIngredientV0> = emptyList(),
    @SerialName("recipeInstructions") val recipeInstructions: List<AddRecipeInstructionV0> = emptyList(),
    @SerialName("slug") val slug: String = "",
    @SerialName("filePath") val filePath: String = "",
    @SerialName("tags") val tags: List<String> = emptyList(),
    @SerialName("categories") val categories: List<String> = emptyList(),
    @SerialName("notes") val notes: List<AddRecipeNoteV0> = emptyList(),
    @SerialName("extras") val extras: Map<String, String> = emptyMap(),
    @SerialName("assets") val assets: List<String> = emptyList(),
    @SerialName("settings") val settings: AddRecipeSettingsV0 = AddRecipeSettingsV0(),
)

