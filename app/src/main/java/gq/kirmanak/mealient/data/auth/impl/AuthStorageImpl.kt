package gq.kirmanak.mealient.data.auth.impl

import android.content.SharedPreferences
import gq.kirmanak.mealient.data.auth.AuthStorage
import gq.kirmanak.mealient.data.impl.util.changesFlow
import gq.kirmanak.mealient.data.impl.util.getStringOrNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
        val baseUrl = sharedPreferences.getStringOrNull(BASE_URL_KEY)
        Timber.d("getBaseUrl: base url is $baseUrl")
        return baseUrl
    }

    override suspend fun getToken(): String? {
        Timber.v("getToken() called")
        val token = sharedPreferences.getStringOrNull(TOKEN_KEY)
        Timber.d("getToken: token is $token")
        return token
    }

    override fun tokenObservable(): Flow<String?> {
        Timber.v("tokenObservable() called")
        return sharedPreferences.changesFlow().map { it.first.getStringOrNull(TOKEN_KEY) }
    }

    override fun clearAuthData() {
        Timber.v("clearAuthData() called")
        sharedPreferences.edit()
            .remove(TOKEN_KEY)
            .remove(BASE_URL_KEY)
            .apply()
    }
}