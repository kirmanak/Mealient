package gq.kirmanak.mealient.ui.auth

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.datasource.NetworkError
import gq.kirmanak.mealient.datasource.runCatchingExceptCancel
import gq.kirmanak.mealient.logging.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class AuthenticationViewModel @Inject constructor(
    private val application: Application,
    private val authRepo: AuthRepo,
    private val logger: Logger,
) : ViewModel() {

    private val _screenState = MutableStateFlow(AuthenticationScreenState())
    val screenState = _screenState.asStateFlow()

    fun onEvent(event: AuthenticationScreenEvent) {
        logger.v { "onEvent() called with: event = $event" }
        when (event) {
            is AuthenticationScreenEvent.OnLoginClick -> {
                onLoginClick()
            }

            is AuthenticationScreenEvent.OnEmailInput -> {
                onEmailInput(event.input)
            }

            is AuthenticationScreenEvent.OnPasswordInput -> {
                onPasswordInput(event.input)
            }

            AuthenticationScreenEvent.TogglePasswordVisibility -> {
                togglePasswordVisibility()
            }
        }
    }

    private fun togglePasswordVisibility() {
        _screenState.update {
            it.copy(isPasswordVisible = !it.isPasswordVisible)
        }
    }

    private fun onPasswordInput(passwordInput: String) {
        _screenState.update {
            it.copy(
                passwordInput = passwordInput,
                buttonEnabled = passwordInput.isNotEmpty() && it.emailInput.isNotEmpty(),
            )
        }
    }

    private fun onEmailInput(emailInput: String) {
        _screenState.update {
            it.copy(
                emailInput = emailInput.trim(),
                buttonEnabled = emailInput.isNotEmpty() && it.passwordInput.isNotEmpty(),
            )
        }
    }

    private fun onLoginClick() {
        val screenState = _screenState.updateAndGet {
            it.copy(
                isLoading = true,
                errorText = null,
                buttonEnabled = false,
            )
        }
        viewModelScope.launch {
            val result = runCatchingExceptCancel {
                authRepo.authenticate(
                    email = screenState.emailInput,
                    password = screenState.passwordInput
                )
            }
            logger.d { "onLoginClick: result = $result" }
            val errorText = result.fold(
                onSuccess = { null },
                onFailure = {
                    when (it) {
                        is NetworkError.Unauthorized -> application.getString(R.string.fragment_authentication_credentials_incorrect)
                        else -> it.message
                    }
                }
            )
            _screenState.update {
                it.copy(
                    isLoading = false,
                    isSuccessful = result.isSuccess,
                    errorText = errorText,
                    buttonEnabled = true,
                )
            }
        }
    }

}