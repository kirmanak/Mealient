package gq.kirmanak.mealient.ui.baseurl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.data.baseurl.ServerInfoRepo
import gq.kirmanak.mealient.data.baseurl.VersionDataSource
import gq.kirmanak.mealient.extensions.runCatchingExceptCancel
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.ui.OperationUiState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BaseURLViewModel @Inject constructor(
    private val serverInfoRepo: ServerInfoRepo,
    private val versionDataSource: VersionDataSource,
    private val logger: Logger,
) : ViewModel() {

    private val _uiState = MutableLiveData<OperationUiState<Unit>>(OperationUiState.Initial())
    val uiState: LiveData<OperationUiState<Unit>> get() = _uiState

    fun saveBaseUrl(baseURL: String) {
        logger.v { "saveBaseUrl() called with: baseURL = $baseURL" }
        _uiState.value = OperationUiState.Progress()
        val hasPrefix = ALLOWED_PREFIXES.any { baseURL.startsWith(it) }
        val url = baseURL.takeIf { hasPrefix } ?: WITH_PREFIX_FORMAT.format(baseURL)
        viewModelScope.launch { checkBaseURL(url) }
    }

    private suspend fun checkBaseURL(baseURL: String) {
        logger.v { "checkBaseURL() called with: baseURL = $baseURL" }
        val result = runCatchingExceptCancel {
            // If it returns proper version info then it must be a Mealie
            val version = versionDataSource.getVersionInfo(baseURL).version
            serverInfoRepo.storeBaseURL(baseURL, version)
        }
        logger.i { "checkBaseURL: result is $result" }
        _uiState.value = OperationUiState.fromResult(result)
    }

    companion object {
        private val ALLOWED_PREFIXES = listOf("http://", "https://")
        private const val WITH_PREFIX_FORMAT = "https://%s"
    }
}