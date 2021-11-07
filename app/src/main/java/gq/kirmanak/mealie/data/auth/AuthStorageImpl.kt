package gq.kirmanak.mealie.data.auth

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

private const val TOKEN_KEY = "AUTH_TOKEN"
private const val BASE_URL_KEY = "BASE_URL"

class AuthStorageImpl @Inject constructor(@ApplicationContext private val context: Context) :
    AuthStorage {
    private val sharedPreferences: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(context)

    override suspend fun storeAuthData(token: String, baseUrl: String) {
        Timber.v("storeAuthData() called with: token = $token, baseUrl = $baseUrl")
        sharedPreferences.edit()
            .putString(TOKEN_KEY, token)
            .putString(BASE_URL_KEY, baseUrl)
            .apply()
    }

    override suspend fun getBaseUrl(): String? {
        Timber.v("getBaseUrl() called")
        val baseUrl = getString(BASE_URL_KEY)
        Timber.d("getBaseUrl: base url is $baseUrl")
        return baseUrl
    }

    override suspend fun getToken(): String? {
        Timber.v("getToken() called")
        val token = getString(TOKEN_KEY)
        Timber.d("getToken: token is $token")
        return token
    }

    private suspend fun getString(key: String): String? = withContext(Dispatchers.Default) {
        sharedPreferences.getString(key, null)
    }
}