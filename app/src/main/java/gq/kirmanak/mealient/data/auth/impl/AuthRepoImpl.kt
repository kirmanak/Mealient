package gq.kirmanak.mealient.data.auth.impl

import gq.kirmanak.mealient.data.auth.AuthDataSource
import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.data.auth.AuthStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class AuthRepoImpl @Inject constructor(
    private val dataSource: AuthDataSource,
    private val storage: AuthStorage
) : AuthRepo {
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

    override suspend fun getBaseUrl(): String? = storage.getBaseUrl()

    override suspend fun getToken(): String? {
        Timber.v("getToken() called")
        return storage.getToken()
    }

    override fun authenticationStatuses(): Flow<Boolean> {
        Timber.v("authenticationStatuses() called")
        return storage.tokenObservable().map { it != null }
    }

    override fun logout() {
        Timber.v("logout() called")
        storage.clearAuthData()
    }
}