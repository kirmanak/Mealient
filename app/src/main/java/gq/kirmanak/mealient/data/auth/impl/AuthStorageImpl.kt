package gq.kirmanak.mealient.data.auth.impl

import android.content.SharedPreferences
import androidx.annotation.VisibleForTesting
import androidx.core.content.edit
import gq.kirmanak.mealient.data.auth.AuthStorage
import gq.kirmanak.mealient.datasource.TokenChangeListener
import gq.kirmanak.mealient.datastore.DataStoreModule.Companion.ENCRYPTED
import gq.kirmanak.mealient.extensions.prefsChangeFlow
import gq.kirmanak.mealient.logging.Logger
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class AuthStorageImpl @Inject constructor(
    @Named(ENCRYPTED) private val sharedPreferences: SharedPreferences,
    private val tokenChangeListener: TokenChangeListener,
    private val logger: Logger,
) : AuthStorage {

    override val authTokenFlow: Flow<String?>
        get() = sharedPreferences
            .prefsChangeFlow(logger) { getString(AUTH_TOKEN_KEY, null) }
            .distinctUntilChanged()
    private val singleThreadDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    override suspend fun setAuthToken(authToken: String?) {
        logger.v { "setAuthToken() called with: authToken = $authToken" }
        tokenChangeListener.onTokenChange()
        putString(AUTH_TOKEN_KEY, authToken)
    }

    override suspend fun getAuthToken(): String? = getString(AUTH_TOKEN_KEY)

    private suspend fun putString(
        key: String,
        value: String?
    ) = withContext(singleThreadDispatcher) {
        logger.v { "putString() called with: key = $key, value = $value" }
        sharedPreferences.edit(commit = true) { putString(key, value) }
    }

    private suspend fun getString(key: String) = withContext(singleThreadDispatcher) {
        val result = sharedPreferences.getString(key, null)
        logger.v { "getString() called with: key = $key, returned: $result" }
        result
    }

    companion object {
        @VisibleForTesting
        const val AUTH_TOKEN_KEY = "authToken"
    }
}