package gq.kirmanak.mealient.data.baseurl.impl

import androidx.core.net.toUri
import gq.kirmanak.mealient.architecture.configuration.AppDispatchers
import gq.kirmanak.mealient.data.storage.PreferencesStorage
import gq.kirmanak.mealient.logging.LogRedactor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class BaseUrlLogRedactor @Inject constructor(
    private val preferencesStorageProvider: Provider<PreferencesStorage>,
    private val dispatchers: AppDispatchers,
) : LogRedactor {

    private val hostState = MutableStateFlow<String?>(null)
    private val preferencesStorage: PreferencesStorage
        get() = preferencesStorageProvider.get()

    init {
        setInitialBaseUrl()
    }

    private fun setInitialBaseUrl() {
        val scope = CoroutineScope(dispatchers.default + SupervisorJob())
        scope.launch {
            hostState.compareAndSet(
                expect = null,
                update = preferencesStorage.getValue(preferencesStorage.baseUrlKey)
            )
        }
    }

    fun set(baseUrl: String) {
        hostState.value = baseUrl.extractHost()
    }


    override fun redact(message: String): String {
        val host = hostState.value
        return when {
            host == null && message.contains(preferencesStorage.baseUrlKey.name) -> "<redacted>"
            host == null -> message
            else -> message.replace(host, "<host>")
        }
    }
}

private fun String.extractHost() = runCatching { toUri() }.getOrNull()?.host
