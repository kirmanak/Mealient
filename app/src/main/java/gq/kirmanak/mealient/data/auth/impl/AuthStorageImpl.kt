package gq.kirmanak.mealient.data.auth.impl

import androidx.datastore.preferences.core.Preferences
import dagger.hilt.android.scopes.ActivityScoped
import gq.kirmanak.mealient.data.auth.AuthStorage
import gq.kirmanak.mealient.data.storage.PreferencesStorage
import gq.kirmanak.mealient.service.auth.AccountManagerInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

@ActivityScoped
class AuthStorageImpl @Inject constructor(
    private val accountManagerInteractorImpl: AccountManagerInteractor,
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

    override fun authHeaderObservable(): Flow<String?> {
        Timber.v("authHeaderObservable() called")
        return accountManagerInteractorImpl.accountUpdatesFlow()
            .map { it.firstOrNull() }
            .map { account ->
                account ?: return@map null
                runCatching { accountManagerInteractorImpl.getAuthToken(account) }
                    .onFailure { Timber.e(it, "authHeaderObservable: can't get token") }
                    .getOrNull()
            }
    }

    override suspend fun clearAuthData() {
        Timber.v("clearAuthData() called")
        preferencesStorage.removeValues(authHeaderKey)
    }
}
