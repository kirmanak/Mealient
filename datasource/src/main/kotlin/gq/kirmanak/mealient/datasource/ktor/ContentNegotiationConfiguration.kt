package gq.kirmanak.mealient.datasource.ktor

import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Inject

class ContentNegotiationConfiguration @Inject constructor(
    private  val json: Json,
) : KtorConfiguration {

    override fun <T : HttpClientEngineConfig> configure(config: HttpClientConfig<T>) {
        config.install(ContentNegotiation) {
            json(json)
        }
    }
}