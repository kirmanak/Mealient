package gq.kirmanak.mealient.datasource.ktor

import gq.kirmanak.mealient.datasource.TokenChangeListener
import gq.kirmanak.mealient.logging.Logger
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.plugin
import javax.inject.Inject

internal class TokenChangeListenerKtor @Inject constructor(
    private val httpClient: HttpClient,
    private val logger: Logger,
) : TokenChangeListener {

    override fun onTokenChange() {
        logger.v { "onTokenChange() called" }
        httpClient.plugin(Auth)
            .providers
            .filterIsInstance<BearerAuthProvider>()
            .forEach {
                logger.d { "onTokenChange(): removing the token" }
                it.clearToken()
            }
    }
}