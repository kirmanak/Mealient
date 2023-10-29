package gq.kirmanak.mealient.datasource.ktor

import gq.kirmanak.mealient.datasource.impl.SslSocketFactoryFactory
import gq.kirmanak.mealient.logging.Logger
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import javax.inject.Inject

internal class KtorClientBuilderImpl @Inject constructor(
    private val configurators: Set<@JvmSuppressWildcards KtorConfiguration>,
    private val logger: Logger,
    private val sslSocketFactoryFactory: SslSocketFactoryFactory,
) : KtorClientBuilder {

    override fun buildKtorClient(): HttpClient {
        logger.v { "buildKtorClient() called" }

        val sslSocketFactory = sslSocketFactoryFactory.create()

        val client = HttpClient(Android) {
            expectSuccess = true

            configurators.forEach {
                it.configure(config = this)
            }

            engine {
                connectTimeout = 30_000 // ms
                socketTimeout = 30_000 // ms

                sslManager = { httpsUrlConnection ->
                    httpsUrlConnection.sslSocketFactory = sslSocketFactory
                }
            }
        }

        return client
    }
}