package gq.kirmanak.mealient.data.baseurl.impl

import androidx.core.net.toUri
import gq.kirmanak.mealient.architecture.configuration.AppDispatchers
import gq.kirmanak.mealient.data.baseurl.ServerInfoStorage
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
    private val serverInfoStorageProvider: Provider<ServerInfoStorage>,
    private val dispatchers: AppDispatchers,
) : LogRedactor {

    private val hostState = MutableStateFlow<String?>(null)

    init {
        setInitialBaseUrl()
    }

    private fun setInitialBaseUrl() {
        val scope = CoroutineScope(dispatchers.default + SupervisorJob())
        scope.launch {
            val serverInfoStorage = serverInfoStorageProvider.get()
            hostState.compareAndSet(
                expect = null,
                update = serverInfoStorage.getBaseURL()?.extractHost(),
            )
        }
    }

    fun set(baseUrl: String) {
        hostState.value = baseUrl.extractHost()
    }


    override fun redact(message: String): String {
        val host = hostState.value ?: return message
        return message.replace(host, "<host>")
    }
}

private fun String.extractHost() = runCatching { toUri() }.getOrNull()?.host
