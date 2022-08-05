package gq.kirmanak.mealient.data.auth.impl

import android.content.SharedPreferences
import androidx.annotation.VisibleForTesting
import androidx.core.content.edit
import gq.kirmanak.mealient.data.auth.AuthStorage
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
    private val logger: Logger,
) : AuthStorage {

    override val authHeaderFlow: Flow<String?>
        get() = sharedPreferences
            .prefsChangeFlow(logger) { getString(AUTH_HEADER_KEY, null) }
            .distinctUntilChanged()
    private val singleThreadDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    override suspend fun setAuthHeader(authHeader: String?) = putString(AUTH_HEADER_KEY, authHeader)

    override suspend fun getAuthHeader(): String? = getString(AUTH_HEADER_KEY)

    override suspend fun setEmail(email: String?) = putString(EMAIL_KEY, email)

    override suspend fun getEmail(): String? = getString(EMAIL_KEY)

    override suspend fun setPassword(password: String?) = putString(PASSWORD_KEY, password)

    override suspend fun getPassword(): String? = getString(PASSWORD_KEY)

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
        const val AUTH_HEADER_KEY = "authHeader"

        @VisibleForTesting
        const val EMAIL_KEY = "email"

        @VisibleForTesting
        const val PASSWORD_KEY = "password"
    }
}