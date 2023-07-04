package gq.kirmanak.mealient.data.auth.impl

import gq.kirmanak.mealient.data.auth.AuthDataSource
import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.data.auth.AuthStorage
import gq.kirmanak.mealient.datasource.AuthenticationProvider
import gq.kirmanak.mealient.logging.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

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
        authStorage.setAuthHeader(AUTH_HEADER_FORMAT.format(token))
        val apiToken = authDataSource.createApiToken(API_TOKEN_NAME)
        authStorage.setAuthHeader(AUTH_HEADER_FORMAT.format(apiToken))
    }

    override suspend fun getAuthHeader(): String? = authStorage.getAuthHeader()

    override suspend fun logout() {
        logger.v { "logout() called" }
        authStorage.setAuthHeader(null)
    }

    companion object {
        private const val AUTH_HEADER_FORMAT = "Bearer %s"
        private const val API_TOKEN_NAME = "Mealient"
    }
}