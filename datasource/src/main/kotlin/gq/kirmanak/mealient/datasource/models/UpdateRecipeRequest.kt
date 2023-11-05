package gq.kirmanak.mealient.datasource.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateRecipeRequest(
    @SerialName("description") val description: String,
    @SerialName("recipeYield") val recipeYield: String,
    @SerialName("recipeIngredient") val recipeIngredient: List<AddRecipeIngredient>,
    @SerialName("recipeInstructions") val recipeInstructions: List<AddRecipeInstruction>,
    @SerialName("settings") val settings: AddRecipeSettings,
)

@Serializable
data class AddRecipeIngredient(
    @SerialName("referenceId") val id: String,
    @SerialName("note") val note: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AddRecipeIngredient

        if (note != other.note) return false

        return true
    }

    override fun hashCode(): Int {
        return note.hashCode()
    }
}

@Serializable
data class AddRecipeInstruction(
    @SerialName("id") val id: String,
    @SerialName("text") val text: String = "",
    @SerialName("ingredientReferences") val ingredientReferences: List<String>,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AddRecipeInstruction

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
data class AddRecipeSettings(
    @SerialName("disableComments") val disableComments: Boolean,
    @SerialName("public") val public: Boolean,
)