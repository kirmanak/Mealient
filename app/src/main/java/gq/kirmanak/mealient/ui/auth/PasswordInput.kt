package gq.kirmanak.mealient.ui.auth

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import gq.kirmanak.mealient.R

@Composable
internal fun PasswordInput(
    input: String,
    errorText: String?,
    isPasswordVisible: Boolean,
    onEvent: (AuthenticationScreenEvent) -> Unit
) {
    val isError = errorText != null

    OutlinedTextField(
        modifier = Modifier
            .semantics { testTag = "password-input" }
            .fillMaxWidth(),
        value = input,
        onValueChange = {
            onEvent(AuthenticationScreenEvent.OnPasswordInput(it))
        },
        label = {
            Text(
                text = stringResource(id = R.string.fragment_authentication_input_hint_password),
            )
        },
        trailingIcon = {
            PasswordTrailingIcon(
                isError = isError,
                isPasswordVisible = isPasswordVisible,
                onEvent = onEvent,
            )
        },
        isError = isError,
        supportingText = {
            Text(
                text = errorText
                    ?: stringResource(id = R.string.fragment_authentication_password_input_helper_text),
            )
        },
        visualTransformation = if (isPasswordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                defaultKeyboardAction(ImeAction.Done)
                onEvent(AuthenticationScreenEvent.OnLoginClick)
            },
        )
    )
}

@Composable
private fun PasswordTrailingIcon(
    isError: Boolean,
    isPasswordVisible: Boolean,
    onEvent: (AuthenticationScreenEvent) -> Unit
) {
    val image = if (isError) {
        Icons.Default.Warning
    } else if (isPasswordVisible) {
        Icons.Default.VisibilityOff
    } else {
        Icons.Default.Visibility
    }
    if (isError) {
        Icon(
            imageVector = image,
            contentDescription = null,
        )
    } else {
        IconButton(
            onClick = {
                onEvent(AuthenticationScreenEvent.TogglePasswordVisibility)
            },
        ) {
            Icon(
                imageVector = image,
                contentDescription = null,
            )
        }
    }
}