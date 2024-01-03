package gq.kirmanak.mealient.ui.baseurl

internal data class BaseURLScreenState(
    val isConfigured: Boolean = false,
    val userInput: String = "",
    val errorText: String? = null,
    val isButtonEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val invalidCertificateDialogState: InvalidCertificateDialogState? = null,
    val isNavigationEnabled: Boolean = true,
) {

    data class InvalidCertificateDialogState(
        val message: String,
        val onAcceptEvent: BaseURLScreenEvent,
    )
}
