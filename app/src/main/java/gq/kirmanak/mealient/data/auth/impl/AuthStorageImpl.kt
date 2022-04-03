package gq.kirmanak.mealient.data.auth.impl

import android.content.SharedPreferences
import androidx.core.content.edit
import gq.kirmanak.mealient.data.auth.AuthStorage
import gq.kirmanak.mealient.extensions.changesFlow
import gq.kirmanak.mealient.extensions.getStringOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

private const val AUTH_HEADER_KEY = "AUTH_TOKEN"
private const val BASE_URL_KEY = "BASE_URL"

class AuthStorageImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : AuthStorage {

    override fun storeAuthData(authHeader: String, baseUrl: String) {
        Timber.v("storeAuthData() called with: authHeader = $authHeader, baseUrl = $baseUrl")
        sharedPreferences.edit {
            putString(AUTH_HEADER_KEY, authHeader)
            putString(BASE_URL_KEY, baseUrl)
        }
    }

    override suspend fun getBaseUrl(): String? {
        val baseUrl = sharedPreferences.getStringOrNull(BASE_URL_KEY)
        Timber.d("getBaseUrl: base url is $baseUrl")
        return baseUrl
    }

    override suspend fun getAuthHeader(): String? {
        Timber.v("getAuthHeader() called")
        val token = sharedPreferences.getStringOrNull(AUTH_HEADER_KEY)
        Timber.d("getAuthHeader: header is \"$token\"")
        return token
    }

    override fun authHeaderObservable(): Flow<String?> {
        Timber.v("authHeaderObservable() called")
        return sharedPreferences.changesFlow().map { it.first.getStringOrNull(AUTH_HEADER_KEY) }
    }

    override fun clearAuthData() {
        Timber.v("clearAuthData() called")
        sharedPreferences.edit {
            remove(AUTH_HEADER_KEY)
            remove(BASE_URL_KEY)
        }
    }
}
