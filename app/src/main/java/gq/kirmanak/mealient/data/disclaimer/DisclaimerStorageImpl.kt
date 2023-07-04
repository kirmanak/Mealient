package gq.kirmanak.mealient.data.disclaimer

import androidx.datastore.preferences.core.Preferences
import gq.kirmanak.mealient.data.storage.PreferencesStorage
import gq.kirmanak.mealient.logging.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DisclaimerStorageImpl @Inject constructor(
    private val preferencesStorage: PreferencesStorage,
    private val logger: Logger,
) : DisclaimerStorage {

    private val isDisclaimerAcceptedKey: Preferences.Key<Boolean>
        get() = preferencesStorage.isDisclaimerAcceptedKey
    override val isDisclaimerAcceptedFlow: Flow<Boolean>
        get() = preferencesStorage.valueUpdates(isDisclaimerAcceptedKey).map { it == true }

    override suspend fun isDisclaimerAccepted(): Boolean {
        logger.v { "isDisclaimerAccepted() called" }
        val isAccepted = preferencesStorage.getValue(isDisclaimerAcceptedKey) ?: false
        logger.v { "isDisclaimerAccepted() returned: $isAccepted" }
        return isAccepted
    }

    override suspend fun acceptDisclaimer() {
        logger.v { "acceptDisclaimer() called" }
        preferencesStorage.storeValues(Pair(isDisclaimerAcceptedKey, true))
    }
}