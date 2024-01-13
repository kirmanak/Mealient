package gq.kirmanak.mealient.ui.baseurl

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.ui.AppTheme
import gq.kirmanak.mealient.ui.preview.ColorSchemePreview

@Composable
internal fun InvalidCertificateDialog(
    state: BaseURLScreenState.InvalidCertificateDialogState,
    onEvent: (BaseURLScreenEvent) -> Unit,
) {
    val onDismiss = {
        onEvent(BaseURLScreenEvent.OnInvalidCertificateDialogDismiss)
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = { onEvent(state.onAcceptEvent) },
            ) {
                Text(text = stringResource(id = R.string.fragment_base_url_invalid_certificate_accept))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
            ) {
                Text(text = stringResource(id = R.string.fragment_base_url_invalid_certificate_deny))
            }
        },
        title = {
            Text(text = stringResource(id = R.string.fragment_base_url_invalid_certificate_title))
        },
        text = {
            Text(text = state.message)
        },
    )
}

@ColorSchemePreview
@Composable
private fun InvalidCertificateDialogPreview() {
    AppTheme {
        InvalidCertificateDialog(
            state = BaseURLScreenState.InvalidCertificateDialogState(
                message = "This is a preview message",
                onAcceptEvent = BaseURLScreenEvent.OnInvalidCertificateDialogDismiss,
            ),
            onEvent = {},
        )
    }
}