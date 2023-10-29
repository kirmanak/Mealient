package gq.kirmanak.mealient.datasource.ktor

import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig

internal interface KtorConfiguration {

    fun <T : HttpClientEngineConfig> configure(config: HttpClientConfig<T>)
}