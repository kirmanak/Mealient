package gq.kirmanak.mealient.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorDetail(@SerialName("detail") val detail: String? = null)