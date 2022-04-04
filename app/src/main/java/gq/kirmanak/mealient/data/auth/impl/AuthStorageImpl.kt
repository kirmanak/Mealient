package gq.kirmanak.mealient.data.auth.impl

import androidx.datastore.preferences.core.Preferences
import gq.kirmanak.mealient.data.auth.AuthStorage
import gq.kirmanak.mealient.data.storage.PreferencesStorage
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthStorageImpl @Inject constructor(
    private val preferencesStorage: PreferencesStorage,
) : AuthStorage {

    private val authHeaderKey: Preferences.Key<String>
        get() = preferencesStorage.authHeaderKey
    override val authHeaderFlow: Flow<String?>
        get() = preferencesStorage.valueUpdates(authHeaderKey)

    override suspend fun storeAuthData(authHeader: String) {
        Timber.v("storeAuthData() called with: authHeader = $authHeader")
        preferencesStorage.storeValues(Pair(authHeaderKey, authHeader))
    }

    override suspend fun getAuthHeader(): String? {
        Timber.v("getAuthHeader() called")
        val token = preferencesStorage.getValue(authHeaderKey)
        Timber.d("getAuthHeader: header is \"$token\"")
        return token
    }

    override suspend fun clearAuthData() {
        Timber.v("clearAuthData() called")
        preferencesStorage.removeValues(authHeaderKey)
    }
}
