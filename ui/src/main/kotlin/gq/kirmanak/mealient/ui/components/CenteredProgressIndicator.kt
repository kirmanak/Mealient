package gq.kirmanak.mealient.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import gq.kirmanak.mealient.ui.AppTheme
import gq.kirmanak.mealient.ui.preview.ColorSchemePreview

@Composable
fun CenteredProgressIndicator(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@ColorSchemePreview
@Composable
fun PreviewCenteredProgressIndicator() {
    AppTheme {
        CenteredProgressIndicator()
    }
}
