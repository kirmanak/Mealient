package gq.kirmanak.mealient.datasource.ktor

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.cache.storage.FileStorage
import java.io.File
import javax.inject.Inject

class CacheKtorConfiguration @Inject constructor(
    @ApplicationContext private val context: Context,
) : KtorConfiguration {

    override fun <T : HttpClientEngineConfig> configure(config: HttpClientConfig<T>) {
        config.install(HttpCache) {
            val file = File(context.cacheDir, "ktor")
            publicStorage(FileStorage(file))
        }
    }
}