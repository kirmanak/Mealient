package gq.kirmanak.mealient.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.extensions.runCatchingExceptCancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authRepo: AuthRepo,
) : ViewModel() {

    private val showLoginButtonFlow = MutableStateFlow(false)
    private val authenticationStateFlow = combine(
        showLoginButtonFlow,
        authRepo.isAuthorizedFlow,
        AuthenticationState::determineState
    )
    val authenticationStateLive: LiveData<AuthenticationState>
        get() = authenticationStateFlow.asLiveData()
    var showLoginButton: Boolean by showLoginButtonFlow::value

    suspend fun authenticate(email: String, password: String) = runCatchingExceptCancel {
        authRepo.authenticate(email, password)
    }.onFailure {
        Timber.e(it, "authenticate: can't authenticate")
    }

    fun logout() {
        Timber.v("logout() called")
        viewModelScope.launch { authRepo.logout() }
    }
}