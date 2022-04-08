package gq.kirmanak.mealient.data.baseurl.impl

import androidx.datastore.preferences.core.Preferences
import gq.kirmanak.mealient.data.baseurl.BaseURLStorage
import gq.kirmanak.mealient.data.storage.PreferencesStorage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseURLStorageImpl @Inject constructor(
    private val preferencesStorage: PreferencesStorage,
) : BaseURLStorage {

    private val baseUrlKey: Preferences.Key<String>
        get() = preferencesStorage.baseUrlKey

    override suspend fun getBaseURL(): String? = preferencesStorage.getValue(baseUrlKey)

    override suspend fun requireBaseURL(): String = checkNotNull(getBaseURL()) {
        "Base URL was null when it was required"
    }

    override suspend fun storeBaseURL(baseURL: String) {
        preferencesStorage.storeValues(Pair(baseUrlKey, baseURL))
    }
}