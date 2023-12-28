package gq.kirmanak.mealient.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.ui.AppTheme
import gq.kirmanak.mealient.ui.Dimens
import gq.kirmanak.mealient.ui.components.TopProgressIndicator
import gq.kirmanak.mealient.ui.preview.ColorSchemePreview

@Destination
@Composable
internal fun AuthenticationScreen(
    navController: NavController,
    viewModel: AuthenticationViewModel = hiltViewModel(),
) {
    val screenState by viewModel.screenState.collectAsState()

    LaunchedEffect(screenState.isSuccessful) {
        if (screenState.isSuccessful) {
            navController.navigateUp()
        }
    }

    AuthenticationScreen(
        state = screenState,
        onEvent = viewModel::onEvent,
    )
}

@Composable
internal fun AuthenticationScreen(
    state: AuthenticationScreenState,
    onEvent: (AuthenticationScreenEvent) -> Unit,
) {
    TopProgressIndicator(
        isLoading = state.isLoading,
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
                input = state.emailInput,
                onEvent = onEvent,
            )

            PasswordInput(
                input = state.passwordInput,
                errorText = state.errorText,
                isPasswordVisible = state.isPasswordVisible,
                onEvent = onEvent,
            )

            Button(
                enabled = state.buttonEnabled,
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
            state = AuthenticationScreenState(),
            onEvent = {},
        )
    }
}