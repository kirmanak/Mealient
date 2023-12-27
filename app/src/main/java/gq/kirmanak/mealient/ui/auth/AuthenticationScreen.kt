package gq.kirmanak.mealient.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.ui.AppTheme
import gq.kirmanak.mealient.ui.Dimens
import gq.kirmanak.mealient.ui.components.TopProgressIndicator
import gq.kirmanak.mealient.ui.preview.ColorSchemePreview

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun AuthenticationScreen(
    screenState: AuthenticationScreenState,
    onEvent: (AuthenticationScreenEvent) -> Unit,
) {
    Scaffold { containerPadding ->
        TopProgressIndicator(
            modifier = Modifier
                .padding(containerPadding)
                .consumeWindowInsets(containerPadding),
            isLoading = screenState.isLoading,
        ) {
            Column(
                modifier = Modifier
                    .padding(Dimens.Medium)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(Dimens.Medium),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.weight(2f))

                EmailInput(
                    input = screenState.emailInput,
                    onEvent = onEvent,
                )

                PasswordInput(
                    input = screenState.passwordInput,
                    errorText = screenState.errorText,
                    isPasswordVisible = screenState.isPasswordVisible,
                    onEvent = onEvent,
                )

                Button(
                    enabled = screenState.buttonEnabled,
                    onClick = { onEvent(AuthenticationScreenEvent.OnLoginClick) },
                ) {
                    Text(
                        text = stringResource(id = R.string.fragment_authentication_button_login),
                    )
                }

                Spacer(modifier = Modifier.weight(8f))
            }
        }
    }
}

@Composable
private fun EmailInput(
    input: String,
    onEvent: (AuthenticationScreenEvent) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),
        value = input,
        onValueChange = {
            onEvent(AuthenticationScreenEvent.OnEmailInput(it))
        },
        label = {
            Text(
                text = stringResource(id = R.string.fragment_authentication_input_hint_email),
            )
        },
        supportingText = {
            Text(
                text = stringResource(id = R.string.fragment_authentication_email_input_helper_text),
            )
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
        )
    )
}

@ColorSchemePreview
@Composable
private fun AuthenticationScreenPreview() {
    AppTheme {
        AuthenticationScreen(
            screenState = AuthenticationScreenState(),
            onEvent = {},
        )
    }
}