package gq.kirmanak.mealient.data.disclaimer

import androidx.datastore.preferences.core.Preferences
import gq.kirmanak.mealient.data.storage.PreferencesStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DisclaimerStorageImpl @Inject constructor(
    private val preferencesStorage: PreferencesStorage,
) : DisclaimerStorage {

    private val isDisclaimerAcceptedKey: Preferences.Key<Boolean>
        get() = preferencesStorage.isDisclaimerAcceptedKey
    override val isDisclaimerAcceptedFlow: Flow<Boolean>
        get() = preferencesStorage.valueUpdates(isDisclaimerAcceptedKey).map { it == true }

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