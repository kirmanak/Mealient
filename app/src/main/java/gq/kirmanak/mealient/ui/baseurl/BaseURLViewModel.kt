package gq.kirmanak.mealient.ui.baseurl

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.data.baseurl.BaseURLStorage
import gq.kirmanak.mealient.data.baseurl.VersionDataSource
import gq.kirmanak.mealient.extensions.runCatchingExceptCancel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class BaseURLViewModel @Inject constructor(
    private val baseURLStorage: BaseURLStorage,
    private val versionDataSource: VersionDataSource,
) : ViewModel() {

    suspend fun saveBaseUrl(baseURL: String): Result<Unit> {
        Timber.v("saveBaseUrl() called with: baseURL = $baseURL")
        val hasPrefix = ALLOWED_PREFIXES.any { baseURL.startsWith(it) }
        val url = baseURL.takeIf { hasPrefix } ?: WITH_PREFIX_FORMAT.format(baseURL)
        return checkBaseURL(url)
    }

    private suspend fun checkBaseURL(baseURL: String): Result<Unit> {
        Timber.v("checkBaseURL() called with: baseURL = $baseURL")
        val result = runCatchingExceptCancel {
            // If it returns proper version info then it must be a Mealie
            versionDataSource.getVersionInfo(baseURL)
        }
        baseURLStorage.storeBaseURL(baseURL)
        return result.map { }
    }

    companion object {
        private val ALLOWED_PREFIXES = listOf("http://", "https://")
        private const val WITH_PREFIX_FORMAT = "https://%s"
    }
}