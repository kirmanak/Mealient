package gq.kirmanak.mealient.datasource.ktor

import gq.kirmanak.mealient.logging.Logger
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import okhttp3.OkHttpClient
import javax.inject.Inject

internal class KtorClientBuilderImpl @Inject constructor(
    private val configurators: Set<@JvmSuppressWildcards KtorConfiguration>,
    private val logger: Logger,
    private val okHttpClient: OkHttpClient,
) : KtorClientBuilder {

    override fun buildKtorClient(): HttpClient {
        logger.v { "buildKtorClient() called" }

        val client = HttpClient(OkHttp) {
            expectSuccess = true

            configurators.forEach {
                it.configure(config = this)
            }

            engine {
                preconfigured = okHttpClient
            }
        }

        return client
    }
}