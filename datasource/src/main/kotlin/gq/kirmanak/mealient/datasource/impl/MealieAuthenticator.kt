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
// TODO has to be interceptor, otherwise only public recipes are visible
class MealieAuthenticator @Inject constructor(
    private val authenticationProviderProvider: Provider<AuthenticationProvider>,
) : Authenticator {

    private val authenticationProvider: AuthenticationProvider
        get() = authenticationProviderProvider.get()

    override fun authenticate(route: Route?, response: Response): Request? {
        val supportsBearer = response.challenges().any { it.scheme == BEARER_SCHEME }
        val request = response.request
        return when {
            request.header(HEADER_NAME) != null -> {
                logout()
                null
            }
            supportsBearer -> getAuthHeader()?.let { request.copyWithHeader(HEADER_NAME, it) }
            else -> null
        }
    }

    private fun getAuthHeader() = runBlocking { authenticationProvider.getAuthHeader() }

    private fun logout() = runBlocking { authenticationProvider.logout() }

    companion object {
        @VisibleForTesting
        const val HEADER_NAME = "Authorization"
        private const val BEARER_SCHEME = "Bearer"
    }
}

private fun Request.copyWithHeader(name: String, value: String): Request {
    return newBuilder().header(name, value).build()
}