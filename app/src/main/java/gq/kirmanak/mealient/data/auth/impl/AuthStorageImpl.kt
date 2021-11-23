package gq.kirmanak.mealient.data.auth.impl

import android.content.SharedPreferences
import gq.kirmanak.mealient.data.auth.AuthStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

private const val TOKEN_KEY = "AUTH_TOKEN"
private const val BASE_URL_KEY = "BASE_URL"

@ExperimentalCoroutinesApi
class AuthStorageImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : AuthStorage {

    override fun storeAuthData(token: String, baseUrl: String) {
        Timber.v("storeAuthData() called with: token = $token, baseUrl = $baseUrl")
        sharedPreferences.edit()
            .putString(TOKEN_KEY, token)
            .putString(BASE_URL_KEY, baseUrl)
            .apply()
    }

    override suspend fun getBaseUrl(): String? {
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

    override fun tokenObservable(): Flow<String?> {
        Timber.v("tokenObservable() called")
        return callbackFlow {
            val listener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
                Timber.v("tokenObservable: listener called with key $key")
                val token = when (key) {
                    null -> null
                    TOKEN_KEY -> prefs.getString(key, null)
                    else -> return@OnSharedPreferenceChangeListener
                }
                Timber.d("tokenObservable: New token: $token")
                trySend(token).onFailure { Timber.e(it, "Can't send new token") }
            }
            Timber.v("tokenObservable: registering listener")
            send(getToken())
            sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
            awaitClose {
                Timber.v("tokenObservable: flow has been closed")
                sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
            }
        }
    }

    override fun clearAuthData() {
        Timber.v("clearAuthData() called")
        sharedPreferences.edit()
            .remove(TOKEN_KEY)
            .remove(BASE_URL_KEY)
            .apply()
    }
}