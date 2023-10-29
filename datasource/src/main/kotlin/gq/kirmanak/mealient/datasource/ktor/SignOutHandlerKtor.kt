package gq.kirmanak.mealient.datasource.ktor

import gq.kirmanak.mealient.datasource.SignOutHandler
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.plugin
import javax.inject.Inject

class SignOutHandlerKtor @Inject constructor(
    private val httpClient: HttpClient,
) : SignOutHandler {

    override fun signOut() {
        httpClient.plugin(Auth)
            .providers
            .filterIsInstance<BearerAuthProvider>()
            .forEach { it.clearToken() }
    }
}