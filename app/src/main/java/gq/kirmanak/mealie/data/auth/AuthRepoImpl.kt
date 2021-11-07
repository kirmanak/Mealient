package gq.kirmanak.mealie.data.auth

import timber.log.Timber
import javax.inject.Inject

class AuthRepoImpl @Inject constructor(
    private val dataSource: AuthDataSource,
    private val storage: AuthStorage
) : AuthRepo {
    override suspend fun isAuthenticated(): Boolean {
        Timber.v("isAuthenticated")
        val authenticated = getToken() != null
        Timber.d("isAuthenticated() response $authenticated")
        return authenticated
    }

    override suspend fun authenticate(
        username: String,
        password: String,
        baseUrl: String
    ): Throwable? {
        Timber.v("authenticate() called with: username = $username, password = $password, baseUrl = $baseUrl")
        val authResult = dataSource.authenticate(username, password, baseUrl)
        Timber.d("authenticate result is $authResult")
        if (authResult.isFailure) return authResult.exceptionOrNull()
        val token = checkNotNull(authResult.getOrNull())
        storage.storeAuthData(token, baseUrl)
        return null
    }

    override suspend fun getBaseUrl(): String? {
        Timber.v("getBaseUrl() called")
        return storage.getBaseUrl()
    }

    override suspend fun getToken(): String? {
        Timber.v("getToken() called")
        return storage.getToken()
    }
}