package gq.kirmanak.mealient.data.disclaimer

import gq.kirmanak.mealient.data.storage.PreferencesStorage
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DisclaimerStorageImpl @Inject constructor(
    private val preferencesStorage: PreferencesStorage,
) : DisclaimerStorage {

    private val isDisclaimerAcceptedKey by preferencesStorage::isDisclaimerAcceptedKey

    override suspend fun isDisclaimerAccepted(): Boolean {
        Timber.v("isDisclaimerAccepted() called")
        val isAccepted = preferencesStorage.getValue(isDisclaimerAcceptedKey) ?: false
        Timber.v("isDisclaimerAccepted() returned: $isAccepted")
        return isAccepted
    }

    override suspend fun acceptDisclaimer() {
        Timber.v("acceptDisclaimer() called")
        preferencesStorage.storeValues(Pair(isDisclaimerAcceptedKey, true))
    }
}