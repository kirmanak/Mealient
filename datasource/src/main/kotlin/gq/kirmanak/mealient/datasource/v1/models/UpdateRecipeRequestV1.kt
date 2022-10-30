package gq.kirmanak.mealient.datasource.v1.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateRecipeRequestV1(
    @SerialName("name") val name: String = "",
    @SerialName("description") val description: String = "",
    @SerialName("image") val image: String = "",
    @SerialName("recipeYield") val recipeYield: String = "",
    @SerialName("recipeIngredient") val recipeIngredient: List<AddRecipeIngredientV1> = emptyList(),
    @SerialName("recipeInstructions") val recipeInstructions: List<AddRecipeInstructionV1> = emptyList(),
    @SerialName("slug") val slug: String = "",
    @SerialName("filePath") val filePath: String = "",
    @SerialName("tags") val tags: List<String> = emptyList(),
    @SerialName("categories") val categories: List<String> = emptyList(),
    @SerialName("notes") val notes: List<AddRecipeNoteV1> = emptyList(),
    @SerialName("extras") val extras: Map<String, String> = emptyMap(),
    @SerialName("assets") val assets: List<String> = emptyList(),
    @SerialName("settings") val settings: AddRecipeSettingsV1 = AddRecipeSettingsV1(),
)

@Serializable
data class AddRecipeIngredientV1(
    @SerialName("disableAmount") val disableAmount: Boolean = true,
    @SerialName("food") val food: String? = null,
    @SerialName("note") val note: String = "",
    @SerialName("quantity") val quantity: Int = 1,
    @SerialName("title") val title: String? = null,
    @SerialName("unit") val unit: String? = null,
)

@Serializable
data class AddRecipeInstructionV1(
    @SerialName("title") val title: String = "",
    @SerialName("text") val text: String = "",
)

@Serializable
data class AddRecipeNoteV1(
    @SerialName("title") val title: String = "",
    @SerialName("text") val text: String = "",
)

@Serializable
data class AddRecipeSettingsV1(
    @SerialName("disableAmount") val disableAmount: Boolean = true,
    @SerialName("disableComments") val disableComments: Boolean = false,
    @SerialName("landscapeView") val landscapeView: Boolean = true,
    @SerialName("public") val public: Boolean = true,
    @SerialName("showAssets") val showAssets: Boolean = true,
    @SerialName("showNutrition") val showNutrition: Boolean = true,
)