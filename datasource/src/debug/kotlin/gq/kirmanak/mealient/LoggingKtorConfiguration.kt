package gq.kirmanak.mealient

import gq.kirmanak.mealient.datasource.BuildConfig
import gq.kirmanak.mealient.datasource.ktor.KtorConfiguration
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import javax.inject.Inject
import gq.kirmanak.mealient.logging.Logger as MealientLogger

class LoggingKtorConfiguration @Inject constructor(
    private val mealientLogger: MealientLogger,
) : KtorConfiguration {

    override fun <T : HttpClientEngineConfig> configure(config: HttpClientConfig<T>) {
        config.install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    mealientLogger.v(throwable = null, tag = "Ktor") { message }
                }
            }
            level = if (BuildConfig.LOG_NETWORK) LogLevel.ALL else LogLevel.INFO
        }
    }
}