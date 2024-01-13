package gq.kirmanak.mealient.ui.baseurl

import java.security.cert.X509Certificate

internal sealed interface BaseURLScreenEvent {

    data object OnProceedClick : BaseURLScreenEvent

    data class OnUserInput(val input: String) : BaseURLScreenEvent

    data object OnInvalidCertificateDialogDismiss : BaseURLScreenEvent

    data class OnInvalidCertificateDialogAccept(
        val certificate: X509Certificate,
    ) : BaseURLScreenEvent
}
