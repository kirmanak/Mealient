package gq.kirmanak.mealient.datasource.v0.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddRecipeSettingsV0(
    @SerialName("disableAmount") val disableAmount: Boolean = true,
    @SerialName("disableComments") val disableComments: Boolean = false,
    @SerialName("landscapeView") val landscapeView: Boolean = true,
    @SerialName("public") val public: Boolean = true,
    @SerialName("showAssets") val showAssets: Boolean = true,
    @SerialName("showNutrition") val showNutrition: Boolean = true,
)