package gq.kirmanak.mealient.ui.baseurl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.data.baseurl.BaseURLStorage
import gq.kirmanak.mealient.data.baseurl.VersionDataSource
import gq.kirmanak.mealient.data.network.NetworkError
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class BaseURLViewModel @Inject constructor(
    private val baseURLStorage: BaseURLStorage,
    private val versionDataSource: VersionDataSource,
) : ViewModel() {

    private val _screenState = MutableLiveData(BaseURLScreenState())
    var currentScreenState: BaseURLScreenState
        get() = _screenState.value!!
        private set(value) {
            _screenState.value = value
        }
    val screenState: LiveData<BaseURLScreenState>
        get() = _screenState

    fun saveBaseUrl(baseURL: String) {
        Timber.v("saveBaseUrl() called with: baseURL = $baseURL")
        val hasPrefix = ALLOWED_PREFIXES.any { baseURL.startsWith(it) }
        val url = baseURL.takeIf { hasPrefix } ?: WITH_PREFIX_FORMAT.format(baseURL)
        viewModelScope.launch { checkBaseURL(url) }
    }

    private suspend fun checkBaseURL(baseURL: String) {
        Timber.v("checkBaseURL() called with: baseURL = $baseURL")
        val version = try {
            // If it returns proper version info then it must be a Mealie
            versionDataSource.getVersionInfo(baseURL)
        } catch (e: NetworkError) {
            Timber.e(e, "checkBaseURL: can't get version info")
            currentScreenState = BaseURLScreenState(e, false)
            return
        }
        Timber.d("checkBaseURL: version is $version")
        baseURLStorage.storeBaseURL(baseURL)
        currentScreenState = BaseURLScreenState(null, true)
    }

    companion object {
        private val ALLOWED_PREFIXES = listOf("http://", "https://")
        private const val WITH_PREFIX_FORMAT = "https://%s"
    }
}