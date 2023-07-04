package gq.kirmanak.mealient.data.baseurl.impl

import androidx.datastore.preferences.core.Preferences
import gq.kirmanak.mealient.data.baseurl.ServerInfoStorage
import gq.kirmanak.mealient.data.storage.PreferencesStorage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ServerInfoStorageImpl @Inject constructor(
    private val preferencesStorage: PreferencesStorage,
) : ServerInfoStorage {

    private val baseUrlKey: Preferences.Key<String>
        get() = preferencesStorage.baseUrlKey

    private val serverVersionKey: Preferences.Key<String>
        get() = preferencesStorage.serverVersionKey

    override suspend fun getBaseURL(): String? = getValue(baseUrlKey)

    override suspend fun storeBaseURL(baseURL: String) {
        preferencesStorage.storeValues(Pair(baseUrlKey, baseURL))
        preferencesStorage.removeValues(serverVersionKey)
    }

    override suspend fun storeBaseURL(baseURL: String?, version: String?) {
        when {
            baseURL == null -> {
                preferencesStorage.removeValues(baseUrlKey, serverVersionKey)
            }

            version != null -> {
                preferencesStorage.storeValues(
                    Pair(baseUrlKey, baseURL), Pair(serverVersionKey, version)
                )
            }

            else -> {
                preferencesStorage.removeValues(serverVersionKey)
                preferencesStorage.storeValues(Pair(baseUrlKey, baseURL))
            }
        }
    }

    override suspend fun getServerVersion(): String? = getValue(serverVersionKey)

    override suspend fun storeServerVersion(version: String) {
        preferencesStorage.storeValues(Pair(serverVersionKey, version))
    }

    override fun serverVersionUpdates(): Flow<String?> {
        return preferencesStorage.valueUpdates(serverVersionKey)
    }

    private suspend fun <T> getValue(key: Preferences.Key<T>): T? = preferencesStorage.getValue(key)

}