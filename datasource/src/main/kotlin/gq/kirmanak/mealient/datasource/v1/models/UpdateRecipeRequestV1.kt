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
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AddRecipeIngredientV1

        if (note != other.note) return false

        return true
    }

    override fun hashCode(): Int {
        return note.hashCode()
    }
}

@Serializable
data class AddRecipeInstructionV1(
    @SerialName("id") val id: String,
    @SerialName("text") val text: String = "",
    @SerialName("ingredientReferences") val ingredientReferences: List<String>,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AddRecipeInstructionV1

        if (text != other.text) return false
        if (ingredientReferences != other.ingredientReferences) return false

        return true
    }

    override fun hashCode(): Int {
        var result = text.hashCode()
        result = 31 * result + ingredientReferences.hashCode()
        return result
    }
}

@Serializable
data class AddRecipeSettingsV1(
    @SerialName("disableComments") val disableComments: Boolean,
    @SerialName("public") val public: Boolean,
)