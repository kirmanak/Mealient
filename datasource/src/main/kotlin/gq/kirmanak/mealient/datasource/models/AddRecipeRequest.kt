package gq.kirmanak.mealient.datasource.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddRecipeRequest(
    @SerialName("name") val name: String = "",
    @SerialName("description") val description: String = "",
    @SerialName("image") val image: String = "",
    @SerialName("recipeYield") val recipeYield: String = "",
    @SerialName("recipeIngredient") val recipeIngredient: List<AddRecipeIngredient> = emptyList(),
    @SerialName("recipeInstructions") val recipeInstructions: List<AddRecipeInstruction> = emptyList(),
    @SerialName("slug") val slug: String = "",
    @SerialName("filePath") val filePath: String = "",
    @SerialName("tags") val tags: List<String> = emptyList(),
    @SerialName("categories") val categories: List<String> = emptyList(),
    @SerialName("notes") val notes: List<AddRecipeNote> = emptyList(),
    @SerialName("extras") val extras: Map<String, String> = emptyMap(),
    @SerialName("assets") val assets: List<String> = emptyList(),
    @SerialName("settings") val settings: AddRecipeSettings = AddRecipeSettings(),
)

@Serializable
data class AddRecipeSettings(
    @SerialName("disableAmount") val disableAmount: Boolean = true,
    @SerialName("disableComments") val disableComments: Boolean = false,
    @SerialName("landscapeView") val landscapeView: Boolean = true,
    @SerialName("public") val public: Boolean = true,
    @SerialName("showAssets") val showAssets: Boolean = true,
    @SerialName("showNutrition") val showNutrition: Boolean = true,
)

@Serializable
data class AddRecipeNote(
    @SerialName("title") val title: String = "",
    @SerialName("text") val text: String = "",
)

@Serializable
data class AddRecipeInstruction(
    @SerialName("title") val title: String = "",
    @SerialName("text") val text: String = "",
)

@Serializable
data class AddRecipeIngredient(
    @SerialName("disableAmount") val disableAmount: Boolean = true,
    @SerialName("food") val food: String? = null,
    @SerialName("note") val note: String = "",
    @SerialName("quantity") val quantity: Int = 1,
    @SerialName("title") val title: String? = null,
    @SerialName("unit") val unit: String? = null,
)
