package gq.kirmanak.mealient.data.auth.impl

import gq.kirmanak.mealient.data.auth.AuthDataSource
import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.data.auth.AuthStorage
import gq.kirmanak.mealient.datasource.AuthenticationProvider
import gq.kirmanak.mealient.datasource.runCatchingExceptCancel
import gq.kirmanak.mealient.logging.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepoImpl @Inject constructor(
    private val authStorage: AuthStorage,
    private val authDataSource: AuthDataSource,
    private val logger: Logger,
) : AuthRepo, AuthenticationProvider {

    override val isAuthorizedFlow: Flow<Boolean>
        get() = authStorage.authHeaderFlow.map { it != null }

    override suspend fun authenticate(email: String, password: String) {
        logger.v { "authenticate() called with: email = $email, password = $password" }
        val token = authDataSource.authenticate(email, password)
        val header = AUTH_HEADER_FORMAT.format(token)
        authStorage.setAuthHeader(header)
        authStorage.setEmail(email)
        authStorage.setPassword(password)
    }

    override suspend fun getAuthHeader(): String? = authStorage.getAuthHeader()

    override suspend fun requireAuthHeader(): String = checkNotNull(getAuthHeader()) {
        "Auth header is null when it was required"
    }

    override suspend fun logout() {
        logger.v { "logout() called" }
        authStorage.setEmail(null)
        authStorage.setPassword(null)
        authStorage.setAuthHeader(null)
    }

    override suspend fun invalidateAuthHeader() {
        logger.v { "invalidateAuthHeader() called" }
        val email = authStorage.getEmail() ?: return
        val password = authStorage.getPassword() ?: return
        runCatchingExceptCancel { authenticate(email, password) }
            .onFailure { logout() } // Clear all known values to avoid reusing them
    }

    companion object {
        private const val AUTH_HEADER_FORMAT = "Bearer %s"
    }
}