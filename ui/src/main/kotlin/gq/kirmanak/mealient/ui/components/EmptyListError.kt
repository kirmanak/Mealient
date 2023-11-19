package gq.kirmanak.mealient.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import gq.kirmanak.mealient.ui.AppTheme
import gq.kirmanak.mealient.ui.Dimens
import gq.kirmanak.mealient.ui.preview.ColorSchemePreview

@Composable
fun EmptyListError(
    text: String,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit = {},
    retryButtonText: String? = null,
) {
    Box(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier.padding(top = Dimens.Medium),
                text = text,
            )
            if (!retryButtonText.isNullOrBlank()) {
                Button(
                    modifier = Modifier.padding(top = Dimens.Medium),
                    onClick = onRetry,
                ) {
                    Text(text = retryButtonText)
                }
            }
        }
    }
}

@Composable
@ColorSchemePreview
fun PreviewEmptyListError() {
    AppTheme {
        EmptyListError(
            text = "No items in the list",
            retryButtonText = "Try again",
            onRetry = {}
        )
    }
}