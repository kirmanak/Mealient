package gq.kirmanak.mealient.data.disclaimer

import kotlinx.coroutines.flow.Flow

interface DisclaimerStorage {

    val isDisclaimerAcceptedFlow: Flow<Boolean>

    suspend fun isDisclaimerAccepted(): Boolean

    suspend fun acceptDisclaimer()
}