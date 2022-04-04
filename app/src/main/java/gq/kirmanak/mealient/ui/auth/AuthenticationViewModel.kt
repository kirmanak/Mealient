package gq.kirmanak.mealient.ui.auth

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.data.auth.AuthRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authRepo: AuthRepo,
) : ViewModel() {

    private val loginRequestsFlow = MutableStateFlow(false)
    val authenticationState: LiveData<AuthenticationState> = loginRequestsFlow.combine(
        flow = authRepo.isAuthorizedFlow,
        transform = AuthenticationState::determineState
    ).asLiveData()

    fun authenticate(username: String, password: String): LiveData<Result<Unit>> {
        Timber.v("authenticate() called with: username = $username, password = $password")
        val result = MutableLiveData<Result<Unit>>()
        viewModelScope.launch {
            runCatching {
                authRepo.authenticate(username, password)
            }.onFailure {
                Timber.e(it, "authenticate: can't authenticate")
                result.value = Result.failure(it)
            }.onSuccess {
                Timber.d("authenticate: authenticated")
                result.value = Result.success(Unit)
            }
        }
        return result
    }

    fun logout() {
        Timber.v("logout() called")
        viewModelScope.launch {
            loginRequestsFlow.emit(false)
            authRepo.logout()
        }
    }

    fun login() {
        Timber.v("login() called")
        viewModelScope.launch { loginRequestsFlow.emit(true) }
    }
}