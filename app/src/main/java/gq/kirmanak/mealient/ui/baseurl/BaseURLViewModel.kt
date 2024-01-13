package gq.kirmanak.mealient.ui.baseurl

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.data.baseurl.ServerInfoRepo
import gq.kirmanak.mealient.data.baseurl.impl.BaseUrlLogRedactor
import gq.kirmanak.mealient.data.recipes.RecipeRepo
import gq.kirmanak.mealient.datasource.CertificateCombinedException
import gq.kirmanak.mealient.datasource.NetworkError
import gq.kirmanak.mealient.datasource.TrustedCertificatesStore
import gq.kirmanak.mealient.datasource.findCauseAsInstanceOf
import gq.kirmanak.mealient.logging.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.security.cert.X509Certificate
import javax.inject.Inject

@HiltViewModel
internal class BaseURLViewModel @Inject constructor(
    private val application: Application,
    private val serverInfoRepo: ServerInfoRepo,
    private val authRepo: AuthRepo,
    private val recipeRepo: RecipeRepo,
    private val logger: Logger,
    private val trustedCertificatesStore: TrustedCertificatesStore,
    private val baseUrlLogRedactor: BaseUrlLogRedactor,
) : AndroidViewModel(application) {

    private val _screenState = MutableStateFlow(BaseURLScreenState())
    val screenState = _screenState.asStateFlow()

    init {
        checkIfNavigationIsAllowed()
    }

    private fun checkIfNavigationIsAllowed() {
        logger.v { "checkIfNavigationIsAllowed() called" }
        viewModelScope.launch {
            val allowed = serverInfoRepo.getUrl() != null
            logger.d { "checkIfNavigationIsAllowed: allowed = $allowed" }
            _screenState.update { it.copy(isNavigationEnabled = allowed) }
        }
    }

    fun saveBaseUrl(baseURL: String) {
        logger.v { "saveBaseUrl() called" }
        _screenState.update {
            it.copy(
                isLoading = true,
                errorText = null,
                invalidCertificateDialogState = null,
                isButtonEnabled = false,
            )
        }
        viewModelScope.launch { checkBaseURL(baseURL) }
    }

    private suspend fun checkBaseURL(baseURL: String) {
        logger.v { "checkBaseURL() called" }

        val hasPrefix = listOf("http://", "https://").any { baseURL.startsWith(it) }
        val urlWithPrefix = baseURL.takeIf { hasPrefix } ?: "https://%s".format(baseURL)
        val url = urlWithPrefix.trimEnd { it == '/' }

        baseUrlLogRedactor.set(baseUrl = url)

        logger.d { "checkBaseURL: Created URL = \"$url\", with prefix = \"$urlWithPrefix\"" }
        if (url == serverInfoRepo.getUrl()) {
            logger.d { "checkBaseURL: new URL matches current" }
            displayCheckUrlSuccess()
            return
        }

        val result: Result<Unit> = serverInfoRepo.tryBaseURL(url).recoverCatching {
            logger.e(it) { "checkBaseURL: trying to recover, had prefix = $hasPrefix" }
            val certificateError = it.findCauseAsInstanceOf<CertificateCombinedException>()
            if (certificateError != null) {
                throw certificateError
            } else if (hasPrefix || it is NetworkError.NotMealie) {
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

        result.fold(
            onSuccess = { displayCheckUrlSuccess() },
            onFailure = { displayCheckUrlError(it) },
        )
    }

    private fun displayCheckUrlSuccess() {
        logger.v { "displayCheckUrlSuccess() called" }
        _screenState.update {
            it.copy(
                isConfigured = true,
                isLoading = false,
                isButtonEnabled = true,
                errorText = null,
                invalidCertificateDialogState = null,
            )
        }
    }

    private fun displayCheckUrlError(exception: Throwable) {
        logger.v { "displayCheckUrlError() called with: exception = $exception" }
        val errorText = getErrorText(exception)
        val invalidCertificateDialogState = if (exception is CertificateCombinedException) {
            buildInvalidCertificateDialog(exception)
        } else {
            null
        }
        _screenState.update {
            it.copy(
                errorText = errorText,
                isButtonEnabled = true,
                isLoading = false,
                invalidCertificateDialogState = invalidCertificateDialogState,
            )
        }
    }

    private fun buildInvalidCertificateDialog(
        exception: CertificateCombinedException,
    ): BaseURLScreenState.InvalidCertificateDialogState {
        logger.v { "buildInvalidCertificateDialog() called with: exception = $exception" }
        val certificate = exception.serverCert
        val message = application.getString(
            R.string.fragment_base_url_invalid_certificate_message,
            certificate.issuerDN.toString(),
            certificate.subjectDN.toString(),
            certificate.notBefore.toString(),
            certificate.notAfter.toString(),
        )
        return BaseURLScreenState.InvalidCertificateDialogState(
            message = message,
            onAcceptEvent = BaseURLScreenEvent.OnInvalidCertificateDialogAccept(
                certificate = exception.serverCert,
            ),
        )
    }

    private fun getErrorText(throwable: Throwable): String {
        logger.v { "getErrorText() called with: throwable = $throwable" }
        return when (throwable) {
            is NetworkError.NoServerConnection -> application.getString(R.string.fragment_base_url_no_connection)
            is NetworkError.NotMealie -> application.getString(R.string.fragment_base_url_unexpected_response)
            is CertificateCombinedException -> application.getString(R.string.fragment_base_url_invalid_certificate_title)
            is NetworkError.MalformedUrl -> {
                val cause = throwable.cause?.message ?: throwable.message
                application.getString(R.string.fragment_base_url_malformed_url, cause)
            }

            else -> application.getString(R.string.fragment_base_url_unknown_error)
        }
    }

    private fun acceptInvalidCertificate(certificate: X509Certificate) {
        logger.v { "acceptInvalidCertificate() called with: certificate = $certificate" }
        trustedCertificatesStore.addTrustedCertificate(certificate)
    }

    fun onEvent(event: BaseURLScreenEvent) {
        logger.v { "onEvent() called with: event = $event" }
        when (event) {
            is BaseURLScreenEvent.OnProceedClick -> {
                saveBaseUrl(_screenState.value.userInput)
            }

            is BaseURLScreenEvent.OnUserInput -> {
                _screenState.update {
                    it.copy(
                        userInput = event.input.trim(),
                        isButtonEnabled = event.input.isNotEmpty(),
                    )
                }
            }

            is BaseURLScreenEvent.OnInvalidCertificateDialogAccept -> {
                _screenState.update {
                    it.copy(
                        invalidCertificateDialogState = null,
                        errorText = null,
                    )
                }
                acceptInvalidCertificate(event.certificate)
            }

            is BaseURLScreenEvent.OnInvalidCertificateDialogDismiss -> {
                _screenState.update { it.copy(invalidCertificateDialogState = null) }
            }
        }
    }
}