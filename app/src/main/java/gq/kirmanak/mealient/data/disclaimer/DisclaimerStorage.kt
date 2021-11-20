package gq.kirmanak.mealient.data.disclaimer

interface DisclaimerStorage {
    suspend fun isDisclaimerAccepted(): Boolean

    fun acceptDisclaimer()
}