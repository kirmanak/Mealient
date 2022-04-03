package gq.kirmanak.mealient.data.auth.impl

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

    private val authHeaderKey by preferencesStorage::authHeaderKey
    private val baseUrlKey by preferencesStorage::baseUrlKey

    override suspend fun storeAuthData(authHeader: String, baseUrl: String) {
        Timber.v("storeAuthData() called with: authHeader = $authHeader, baseUrl = $baseUrl")
        preferencesStorage.storeValues(
            Pair(authHeaderKey, authHeader),
            Pair(baseUrlKey, baseUrl),
        )
    }

    override suspend fun getBaseUrl(): String? {
        val baseUrl = preferencesStorage.getValue(baseUrlKey)
        Timber.d("getBaseUrl: base url is $baseUrl")
        return baseUrl
    }

    override suspend fun getAuthHeader(): String? {
        Timber.v("getAuthHeader() called")
        val token = preferencesStorage.getValue(authHeaderKey)
        Timber.d("getAuthHeader: header is \"$token\"")
        return token
    }

    override fun authHeaderObservable(): Flow<String?> {
        Timber.v("authHeaderObservable() called")
        return preferencesStorage.valueUpdates(authHeaderKey)
    }

    override suspend fun clearAuthData() {
        Timber.v("clearAuthData() called")
        preferencesStorage.removeValues(authHeaderKey, baseUrlKey)
    }
}
