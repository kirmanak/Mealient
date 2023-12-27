package gq.kirmanak.mealient.ui.baseurl

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.input.ImeAction
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.ui.AppTheme
import gq.kirmanak.mealient.ui.Dimens
import gq.kirmanak.mealient.ui.components.TopProgressIndicator
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
        TopProgressIndicator(
            modifier = Modifier
                .padding(containerPadding)
                .consumeWindowInsets(containerPadding),
            isLoading = state.isLoading,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Dimens.Large),
                verticalArrangement = Arrangement.spacedBy(Dimens.Large),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.weight(3f))

                UrlInputField(
                    input = state.userInput,
                    errorText = state.errorText,
                    onEvent = onEvent,
                    isError = state.errorText != null,
                )

                Button(
                    modifier = Modifier
                        .semantics { testTag = "proceed-button" },
                    onClick = { onEvent(BaseURLScreenEvent.OnProceedClick) },
                    enabled = state.isButtonEnabled,
                ) {
                    Text(
                        modifier = Modifier
                            .semantics { testTag = "proceed-button-text" },
                        text = stringResource(id = R.string.fragment_base_url_save),
                    )
                }

                Spacer(modifier = Modifier.weight(7f))
            }
        }
    }
}

@Composable
private fun UrlInputField(
    input: String,
    errorText: String?,
    onEvent: (BaseURLScreenEvent) -> Unit,
    isError: Boolean
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .semantics { testTag = "url-input-field" },
        value = input,
        isError = isError,
        label = {
            Text(
                modifier = Modifier
                    .semantics { testTag = "url-input-label" },
                text = stringResource(id = R.string.fragment_authentication_input_hint_url),
            )
        },
        supportingText = {
            Text(
                text = errorText
                    ?: stringResource(id = R.string.fragment_base_url_url_input_helper_text),
            )
        },
        onValueChange = {
            onEvent(BaseURLScreenEvent.OnUserInput(it))
        },
        trailingIcon = {
            if (isError) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                defaultKeyboardAction(ImeAction.Done)
                onEvent(BaseURLScreenEvent.OnProceedClick)
            },
        )
    )
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