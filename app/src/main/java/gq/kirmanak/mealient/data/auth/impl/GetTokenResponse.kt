package gq.kirmanak.mealient.data.auth.impl

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetTokenResponse(@SerialName("access_token") val accessToken: String)