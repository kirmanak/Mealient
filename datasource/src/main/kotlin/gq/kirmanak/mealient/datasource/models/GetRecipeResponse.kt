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
    @SerialName("rating") val rating: Long? = null,
    @SerialName("userId") val userId: String,
    @SerialName("groupId") val groupId: String,
    @SerialName("totalTime") val totalTime: String = "",
    @SerialName("prepTime") val prepTime: String = "",
    @SerialName("cookTime") val cookTime: String = "",
    @SerialName("performTime") val performTime: String = "",
)

@Serializable
data class GetRecipeSettingsResponse(
    @SerialName("id") val remoteId: String,
    @SerialName("public") val public: Boolean,
    @SerialName("showNutrition") val showNutrition: Boolean,
    @SerialName("showAssets") val showAssets: Boolean,
    @SerialName("landscapeView") val landscapeView: Boolean,
    @SerialName("disableComments") val disableComments: Boolean,
    @SerialName("disableAmount") val disableAmount: Boolean,
    @SerialName("locked") val locked: Boolean,
)

@Serializable
data class GetRecipeIngredientResponse(
    @SerialName("note") val note: String = "",
    @SerialName("unit") val unit: GetUnitResponse?,
    @SerialName("food") val food: GetFoodResponse?,
    @SerialName("quantity") val quantity: Double?,
    @SerialName("display") val display: String,
    @SerialName("referenceId") val referenceId: String,
    @SerialName("title") val title: String?,
    @SerialName("isFood") val isFood: Boolean,
    @SerialName("disableAmount") val disableAmount: Boolean,
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