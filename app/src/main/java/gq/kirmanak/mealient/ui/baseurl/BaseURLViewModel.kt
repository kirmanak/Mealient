package gq.kirmanak.mealient.ui.baseurl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.data.baseurl.ServerInfoRepo
import gq.kirmanak.mealient.data.recipes.RecipeRepo
import gq.kirmanak.mealient.datasource.NetworkError
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.ui.OperationUiState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BaseURLViewModel @Inject constructor(
    private val serverInfoRepo: ServerInfoRepo,
    private val authRepo: AuthRepo,
    private val recipeRepo: RecipeRepo,
    private val logger: Logger,
) : ViewModel() {

    private val _uiState = MutableLiveData<OperationUiState<Unit>>(OperationUiState.Initial())
    val uiState: LiveData<OperationUiState<Unit>> get() = _uiState

    fun saveBaseUrl(baseURL: String) {
        logger.v { "saveBaseUrl() called with: baseURL = $baseURL" }
        _uiState.value = OperationUiState.Progress()
        viewModelScope.launch { checkBaseURL(baseURL) }
    }

    private suspend fun checkBaseURL(baseURL: String) {
        logger.v { "checkBaseURL() called with: baseURL = $baseURL" }

        val hasPrefix = listOf("http://", "https://").any { baseURL.startsWith(it) }
        val urlWithPrefix = baseURL.takeIf { hasPrefix } ?: "https://%s".format(baseURL)
        val url = urlWithPrefix.trimEnd { it == '/' }

        logger.d { "checkBaseURL: Created URL = \"$url\", with prefix = \"$urlWithPrefix\"" }
        if (url == serverInfoRepo.getUrl()) {
            logger.d { "checkBaseURL: new URL matches current" }
            _uiState.value = OperationUiState.fromResult(Result.success(Unit))
            return
        }

        val result: Result<Unit> = serverInfoRepo.tryBaseURL(url).recoverCatching {
            logger.e(it) { "checkBaseURL: trying to recover, had prefix = $hasPrefix" }
            if (hasPrefix || it is NetworkError.NotMealie) {
                throw it
            } else {
                val unencryptedUrl = url.replace("https", "http")
                serverInfoRepo.tryBaseURL(unencryptedUrl).getOrThrow()
            }
        }

        if (result.isSuccess) {
            authRepo.logout()
            recipeRepo.clearLocalData()
        }

        logger.i { "checkBaseURL: result is $result" }
        _uiState.value = OperationUiState.fromResult(result)
    }

}