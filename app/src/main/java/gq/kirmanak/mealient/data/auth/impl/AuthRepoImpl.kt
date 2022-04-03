package gq.kirmanak.mealient.data.auth.impl

import android.net.Uri
import androidx.annotation.VisibleForTesting
import gq.kirmanak.mealient.data.auth.AuthDataSource
import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.data.auth.AuthStorage
import gq.kirmanak.mealient.data.auth.impl.AuthenticationError.MalformedUrl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.HttpUrl.Companion.toHttpUrl
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepoImpl @Inject constructor(
    private val dataSource: AuthDataSource,
    private val storage: AuthStorage,
) : AuthRepo {

    override suspend fun authenticate(
        username: String,
        password: String,
        baseUrl: String
    ) {
        Timber.v("authenticate() called with: username = $username, password = $password, baseUrl = $baseUrl")
        val url = parseBaseUrl(baseUrl)
        val accessToken = dataSource.authenticate(username, password, url)
        Timber.d("authenticate result is \"$accessToken\"")
        storage.storeAuthData(AUTH_HEADER_FORMAT.format(accessToken), url)
    }

    override suspend fun getBaseUrl(): String? = storage.getBaseUrl()

    override suspend fun requireBaseUrl(): String =
        checkNotNull(getBaseUrl()) { "Base URL is null when it was required" }

    override suspend fun getAuthHeader(): String? = storage.getAuthHeader()

    override suspend fun requireAuthHeader(): String =
        checkNotNull(getAuthHeader()) { "Auth header is null when it was required" }

    override fun authenticationStatuses(): Flow<Boolean> {
        Timber.v("authenticationStatuses() called")
        return storage.authHeaderObservable().map { it != null }
    }

    override suspend fun logout() {
        Timber.v("logout() called")
        storage.clearAuthData()
    }

    @VisibleForTesting
    fun parseBaseUrl(baseUrl: String): String = try {
        val withScheme = Uri.parse(baseUrl).let {
            if (it.scheme == null) it.buildUpon().scheme("https").build()
            else it
        }.toString()
        withScheme.toHttpUrl().toString()
    } catch (e: Throwable) {
        Timber.e(e, "authenticate: can't parse base url $baseUrl")
        throw MalformedUrl(e)
    }

    companion object {
        private const val AUTH_HEADER_FORMAT = "Bearer %s"
    }
}