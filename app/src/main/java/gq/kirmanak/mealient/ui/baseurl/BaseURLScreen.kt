package gq.kirmanak.mealient.ui.baseurl

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.ui.AppTheme
import gq.kirmanak.mealient.ui.Dimens
import gq.kirmanak.mealient.ui.components.BaseScreen
import gq.kirmanak.mealient.ui.components.BaseScreenState
import gq.kirmanak.mealient.ui.components.BaseScreenWithNavigation
import gq.kirmanak.mealient.ui.components.TopProgressIndicator
import gq.kirmanak.mealient.ui.components.previewBaseScreenState
import gq.kirmanak.mealient.ui.preview.ColorSchemePreview

@Destination
@Composable
internal fun BaseURLScreen(
    navController: NavController,
    baseScreenState: BaseScreenState,
    viewModel: BaseURLViewModel = hiltViewModel(),
) {
    val screenState by viewModel.screenState.collectAsState()

    LaunchedEffect(screenState.isConfigured) {
        if (screenState.isConfigured) {
            navController.navigateUp()
        }
    }

    BaseURLScreen(
        state = screenState,
        baseScreenState = baseScreenState,
        onEvent = viewModel::onEvent,
    )
}

@Composable
private fun BaseURLScreen(
    state: BaseURLScreenState,
    baseScreenState: BaseScreenState,
    onEvent: (BaseURLScreenEvent) -> Unit,
) {
    val content: @Composable (Modifier) -> Unit = {
        BaseURLScreen(
            modifier = it,
            state = state,
            onEvent = onEvent,
        )
    }

    if (state.isNavigationEnabled) {
        BaseScreenWithNavigation(
            baseScreenState = baseScreenState,
            content = content,
        )
    } else {
        BaseScreen(
            content = content,
        )
    }
}

@Composable
private fun BaseURLScreen(
    state: BaseURLScreenState,
    modifier: Modifier = Modifier,
    onEvent: (BaseURLScreenEvent) -> Unit,
) {
    if (state.invalidCertificateDialogState != null) {
        InvalidCertificateDialog(
            state = state.invalidCertificateDialogState,
            onEvent = onEvent,
        )
    }

    TopProgressIndicator(
        modifier = modifier,
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

@Composable
private fun UrlInputField(
    input: String,
    errorText: String?,
    onEvent: (BaseURLScreenEvent) -> Unit,
    isError: Boolean,
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
                isNavigationEnabled = false,
            ),
            baseScreenState = previewBaseScreenState(),
            onEvent = {},
        )
    }
}