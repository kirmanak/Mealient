package gq.kirmanak.mealient.ui.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.ui.auth.AuthenticationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val authRepo: AuthRepo,
) : ViewModel() {

    private val showLoginButtonFlow = MutableStateFlow(false)
    var showLoginButton: Boolean by showLoginButtonFlow::value

    private val authenticationStateFlow = combine(
        showLoginButtonFlow,
        authRepo.isAuthorizedFlow,
        AuthenticationState::determineState
    )
    val authenticationStateLive = authenticationStateFlow.asLiveData()

    fun logout() {
        Timber.v("logout() called")
        viewModelScope.launch { authRepo.logout() }
    }
}