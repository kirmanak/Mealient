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

class AuthStorageImpl @Inject constructor(@ApplicationContext private val context: Context) : AuthStorage {
    private val sharedPreferences: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(context)

    override suspend fun isAuthenticated(): Boolean = withContext(Dispatchers.IO) {
        Timber.v("isAuthenticated() called")
        val token = sharedPreferences.getString(TOKEN_KEY, null)
        Timber.d("isAuthenticated: token is $token")
        token != null
    }

    override suspend fun storeToken(token: String) {
        Timber.d("storeToken() called with: token = $token")
        sharedPreferences.edit().putString(TOKEN_KEY, token).apply()
    }
}