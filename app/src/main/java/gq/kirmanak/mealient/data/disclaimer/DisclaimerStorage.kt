package gq.kirmanak.mealient.data.disclaimer

interface DisclaimerStorage {
    suspend fun isDisclaimerAccepted(): Boolean

    suspend fun acceptDisclaimer()
}