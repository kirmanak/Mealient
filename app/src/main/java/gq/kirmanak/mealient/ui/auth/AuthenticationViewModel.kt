package gq.kirmanak.mealient.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.extensions.runCatchingExceptCancel
import gq.kirmanak.mealient.ui.OperationUiState
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authRepo: AuthRepo,
) : ViewModel() {

    private val _uiState = MutableLiveData<OperationUiState<Unit>>(OperationUiState.Initial())
    val uiState: LiveData<OperationUiState<Unit>> get() = _uiState

    fun authenticate(email: String, password: String) {
        Timber.v("authenticate() called with: email = $email, password = $password")
        _uiState.value = OperationUiState.Progress()
        viewModelScope.launch {
            val result = runCatchingExceptCancel { authRepo.authenticate(email, password) }
            _uiState.value = OperationUiState.fromResult(result)
        }
    }
}