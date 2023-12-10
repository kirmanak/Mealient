package gq.kirmanak.mealient.data.auth.impl

import gq.kirmanak.mealient.data.auth.AuthDataSource
import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.data.auth.AuthStorage
import gq.kirmanak.mealient.datasource.AuthenticationProvider
import gq.kirmanak.mealient.datasource.SignOutHandler
import gq.kirmanak.mealient.logging.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthRepoImpl @Inject constructor(
    private val authStorage: AuthStorage,
    private val authDataSource: AuthDataSource,
    private val logger: Logger,
    private val signOutHandler: SignOutHandler,
    private val credentialsLogRedactor: CredentialsLogRedactor,
) : AuthRepo, AuthenticationProvider {

    override val isAuthorizedFlow: Flow<Boolean>
        get() = authStorage.authTokenFlow.map { it != null }

    override suspend fun authenticate(email: String, password: String) {
        logger.v { "authenticate() called" }
        credentialsLogRedactor.set(email, password)
        val token = authDataSource.authenticate(email, password)
        authStorage.setAuthToken(token)
        val apiToken = authDataSource.createApiToken(API_TOKEN_NAME)
        authStorage.setAuthToken(apiToken)
        credentialsLogRedactor.clear()
    }

    override suspend fun getAuthToken(): String? = authStorage.getAuthToken()

    override suspend fun logout() {
        logger.v { "logout() called" }
        authStorage.setAuthToken(null)
        signOutHandler.signOut()
    }

    companion object {
        private const val API_TOKEN_NAME = "Mealient"
    }
}