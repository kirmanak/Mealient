package gq.kirmanak.mealient.ui.activity

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.logging.Logger
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val authRepo: AuthRepo,
    private val logger: Logger,
) : ViewModel() {

    private val _uiState = MutableLiveData(MainActivityUiState())
    val uiStateLive: LiveData<MainActivityUiState>
        get() = _uiState.distinctUntilChanged()
    var uiState: MainActivityUiState
        get() = checkNotNull(_uiState.value) { "UiState must not be null" }
        private set(value) = _uiState.postValue(value)

    init {
        authRepo.isAuthorizedFlow
            .onEach { isAuthorized -> updateUiState { it.copy(isAuthorized = isAuthorized) } }
            .launchIn(viewModelScope)
    }

    fun updateUiState(updater: (MainActivityUiState) -> MainActivityUiState) {
        uiState = updater(uiState)
    }

    fun logout() {
        logger.v { "logout() called" }
        viewModelScope.launch { authRepo.logout() }
    }
}