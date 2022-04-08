package gq.kirmanak.mealient.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.extensions.runCatchingExceptCancel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authRepo: AuthRepo,
) : ViewModel() {

    private val _authenticationResult = MutableLiveData<Result<Unit>>()
    val authenticationResult: LiveData<Result<Unit>>
        get() = _authenticationResult

    fun authenticate(email: String, password: String) {
        Timber.v("authenticate() called with: email = $email, password = $password")
        viewModelScope.launch {
            _authenticationResult.value = runCatchingExceptCancel {
                authRepo.authenticate(email, password)
            }
        }
    }
}