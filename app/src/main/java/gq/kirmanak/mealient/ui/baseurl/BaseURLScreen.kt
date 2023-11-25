package gq.kirmanak.mealient.ui.baseurl

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.ui.AppTheme
import gq.kirmanak.mealient.ui.Dimens
import gq.kirmanak.mealient.ui.preview.ColorSchemePreview

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun BaseURLScreen(
    state: BaseURLScreenState,
    onEvent: (BaseURLScreenEvent) -> Unit,
) {
    if (state.invalidCertificateDialogState != null) {
        InvalidCertificateDialog(
            state = state.invalidCertificateDialogState,
            onEvent = onEvent,
        )
    }

    Scaffold { containerPadding ->
        Box(
            modifier = Modifier
                .padding(containerPadding)
                .consumeWindowInsets(containerPadding),
        ) {
            if (state.isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter),
                )
            }

            BaseURLScreenContent(
                modifier = Modifier
                    .fillMaxSize(),
                state = state,
                onEvent = onEvent,
            )
        }
    }
}

@Composable
private fun BaseURLScreenContent(
    state: BaseURLScreenState,
    onEvent: (BaseURLScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(Dimens.Large),
        verticalArrangement = Arrangement.spacedBy(Dimens.Large),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.weight(3f))

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = state.userInput,
            isError = state.errorText != null,
            label = {
                Text(
                    text = stringResource(id = R.string.fragment_authentication_input_hint_url),
                )
            },
            supportingText = {
                Text(
                    text = state.errorText
                        ?: stringResource(id = R.string.fragment_base_url_url_input_helper_text),
                )
            },
            onValueChange = {
                onEvent(BaseURLScreenEvent.OnUserInput(it))
            },
            trailingIcon = {
                if (state.errorText != null) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                    )
                }
            }
        )

        Button(
            onClick = { onEvent(BaseURLScreenEvent.OnProceedClick) },
            enabled = state.isButtonEnabled,
        ) {
            Text(
                text = stringResource(id = R.string.fragment_base_url_save),
            )
        }

        Spacer(modifier = Modifier.weight(7f))
    }
}

@ColorSchemePreview
@Composable
private fun BaseURLScreenPreview() {
    AppTheme {
        BaseURLScreen(
            state = BaseURLScreenState(
                userInput = "https://www.google.com",
                errorText = null,
                isButtonEnabled = true,
                isLoading = true,
            ),
            onEvent = {},
        )
    }
}