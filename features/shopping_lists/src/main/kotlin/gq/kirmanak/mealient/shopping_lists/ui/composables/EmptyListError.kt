package gq.kirmanak.mealient.shopping_lists.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import gq.kirmanak.mealient.shopping_list.R
import gq.kirmanak.mealient.ui.AppTheme
import gq.kirmanak.mealient.ui.Dimens

@Composable
fun EmptyListError(
    loadError: Throwable?,
    onRetry: () -> Unit,
    defaultError: String,
    modifier: Modifier = Modifier,
) {
    val text = loadError?.let { getErrorMessage(it) } ?: defaultError
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
            Button(
                modifier = Modifier.padding(top = Dimens.Medium),
                onClick = onRetry,
            ) {
                Text(text = stringResource(id = R.string.shopping_lists_screen_empty_button_refresh))
            }
        }
    }
}

@Composable
@Preview
fun PreviewEmptyListError() {
    AppTheme {
        EmptyListError(
            loadError = null,
            onRetry = {},
            defaultError = "No items in the list"
        )
    }
}