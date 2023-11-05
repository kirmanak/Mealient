package gq.kirmanak.mealient.datasource.ktor

import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.compression.ContentEncoding
import javax.inject.Inject

internal class EncodingKtorConfiguration @Inject constructor() : KtorConfiguration {

    override fun <T : HttpClientEngineConfig> configure(config: HttpClientConfig<T>) {
        config.install(ContentEncoding) {
            gzip()
            deflate()
            identity()
        }
    }
}