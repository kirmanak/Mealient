package gq.kirmanak.mealient.shopping_lists.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import gq.kirmanak.mealient.ui.AppTheme

@Composable
fun CenteredText(
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = text)
    }
}

@Preview
@Composable
fun PreviewCenteredText() {
    AppTheme {
        CenteredText(text = "Hello World")
    }
}