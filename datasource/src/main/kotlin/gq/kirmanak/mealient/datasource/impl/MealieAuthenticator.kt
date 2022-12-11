package gq.kirmanak.mealient.datasource.impl

import androidx.annotation.VisibleForTesting
import gq.kirmanak.mealient.datasource.AuthenticationProvider
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class MealieAuthenticator @Inject constructor(
    private val authenticationProvider: Provider<AuthenticationProvider>,
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        val supportsBearer = response.challenges().any { it.scheme == BEARER_SCHEME }
        val request = response.request
        return if (supportsBearer && request.header(HEADER_NAME) == null) {
            getAuthHeader()?.let { request.copyWithHeader(HEADER_NAME, it) }
        } else {
            null // Either Bearer is not supported or we've already tried to authenticate
        }
    }

    private fun getAuthHeader() = runBlocking { authenticationProvider.get().getAuthHeader() }

    companion object {
        @VisibleForTesting
        const val HEADER_NAME = "Authorization"
        private const val BEARER_SCHEME = "Bearer"
    }
}

private fun Request.copyWithHeader(name: String, value: String): Request {
    return newBuilder().header(name, value).build()
}