package gq.kirmanak.mealient.ui.activity

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.data.baseurl.ServerInfoRepo
import gq.kirmanak.mealient.data.disclaimer.DisclaimerStorage
import gq.kirmanak.mealient.data.recipes.RecipeRepo
import gq.kirmanak.mealient.logging.Logger
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val authRepo: AuthRepo,
    private val logger: Logger,
    private val disclaimerStorage: DisclaimerStorage,
    private val serverInfoRepo: ServerInfoRepo,
    private val recipeRepo: RecipeRepo,
) : ViewModel() {

    private val _uiState = MutableLiveData(MainActivityUiState())
    val uiStateLive: LiveData<MainActivityUiState>
        get() = _uiState.distinctUntilChanged()
    var uiState: MainActivityUiState
        get() = checkNotNull(_uiState.value) { "UiState must not be null" }
        private set(value) = _uiState.postValue(value)

    private val _startDestination = MutableLiveData<Int>()
    val startDestination: LiveData<Int> = _startDestination

    private val _clearSearchViewFocusChannel = Channel<Unit>()
    val clearSearchViewFocus: Flow<Unit> = _clearSearchViewFocusChannel.receiveAsFlow()

    init {
        authRepo.isAuthorizedFlow
            .onEach { isAuthorized -> updateUiState { it.copy(isAuthorized = isAuthorized) } }
            .launchIn(viewModelScope)

        viewModelScope.launch {
            _startDestination.value = when {
                !disclaimerStorage.isDisclaimerAccepted() -> R.id.disclaimerFragment
                serverInfoRepo.getUrl() == null -> R.id.baseURLFragment
                else -> R.id.recipesListFragment
            }
        }
    }

    fun updateUiState(updater: (MainActivityUiState) -> MainActivityUiState) {
        uiState = updater(uiState)
    }

    fun logout() {
        logger.v { "logout() called" }
        viewModelScope.launch { authRepo.logout() }
    }

    fun onSearchQuery(query: String?) {
        logger.v { "onSearchQuery() called with: query = $query" }
        recipeRepo.updateNameQuery(query)
    }

    fun clearSearchViewFocus() {
        logger.v { "clearSearchViewFocus() called" }
        _clearSearchViewFocusChannel.trySend(Unit)
    }
}