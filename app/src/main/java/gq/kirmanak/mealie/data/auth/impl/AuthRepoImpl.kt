package gq.kirmanak.mealie.data.auth.impl

import gq.kirmanak.mealie.data.auth.AuthDataSource
import gq.kirmanak.mealie.data.auth.AuthRepo
import gq.kirmanak.mealie.data.auth.AuthStorage
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
    ) {
        Timber.v("authenticate() called with: username = $username, password = $password, baseUrl = $baseUrl")
        val url = if (baseUrl.startsWith("http")) baseUrl else "https://$baseUrl"
        val accessToken = dataSource.authenticate(username, password, url)
        Timber.d("authenticate result is $accessToken")
        storage.storeAuthData(accessToken, url)
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