package gq.kirmanak.mealient.data.auth.impl

import gq.kirmanak.mealient.data.auth.AuthDataSource
import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.data.auth.AuthStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepoImpl @Inject constructor(
    private val dataSource: AuthDataSource,
    private val storage: AuthStorage,
) : AuthRepo {

    override val isAuthorizedFlow: Flow<Boolean>
        get() = storage.authHeaderFlow.map { it != null }

    override suspend fun authenticate(username: String, password: String) {
        Timber.v("authenticate() called with: username = $username, password = $password")
        val accessToken = dataSource.authenticate(username, password)
        Timber.d("authenticate result is \"$accessToken\"")
        storage.storeAuthData(AUTH_HEADER_FORMAT.format(accessToken))
    }

    override suspend fun getAuthHeader(): String? = storage.getAuthHeader()

    override suspend fun requireAuthHeader(): String =
        checkNotNull(getAuthHeader()) { "Auth header is null when it was required" }

    override suspend fun logout() {
        Timber.v("logout() called")
        storage.clearAuthData()
    }

    companion object {
        private const val AUTH_HEADER_FORMAT = "Bearer %s"
    }
}