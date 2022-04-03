package gq.kirmanak.mealient.data.baseurl

import gq.kirmanak.mealient.data.storage.PreferencesStorage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseURLStorageImpl @Inject constructor(
    private val preferencesStorage: PreferencesStorage,
) : BaseURLStorage {

    private val baseUrlKey by preferencesStorage::baseUrlKey

    override suspend fun getBaseURL(): String? = preferencesStorage.getValue(baseUrlKey)

    override suspend fun requireBaseURL(): String = checkNotNull(getBaseURL()) {
        "Base URL was null when it was required"
    }

    override suspend fun storeBaseURL(baseURL: String) {
        preferencesStorage.storeValues(Pair(baseUrlKey, baseURL))
    }
}