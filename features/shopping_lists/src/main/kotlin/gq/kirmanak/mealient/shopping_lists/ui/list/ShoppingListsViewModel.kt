package gq.kirmanak.mealient.shopping_lists.ui.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.architecture.valueUpdatesOnly
import gq.kirmanak.mealient.datasource.models.GetShoppingListsSummaryResponse
import gq.kirmanak.mealient.datasource.runCatchingExceptCancel
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.shopping_lists.repo.ShoppingListsAuthRepo
import gq.kirmanak.mealient.shopping_lists.repo.ShoppingListsRepo
import gq.kirmanak.mealient.ui.util.LoadingHelper
import gq.kirmanak.mealient.ui.util.LoadingHelperFactory
import gq.kirmanak.mealient.ui.util.LoadingState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingListsViewModel @Inject constructor(
    private val logger: Logger,
    private val shoppingListsRepo: ShoppingListsRepo,
    private val authRepo: ShoppingListsAuthRepo,
    loadingHelperFactory: LoadingHelperFactory,
) : ViewModel() {

    private val newShoppingLists = MutableStateFlow<List<String>>(emptyList())

    private val loadingHelper: LoadingHelper<List<GetShoppingListsSummaryResponse>> =
        loadingHelperFactory.create(viewModelScope) {
            runCatchingExceptCancel { shoppingListsRepo.getShoppingLists() }
        }

    private val screenState = MutableStateFlow(
        ScreenState(loadingState = loadingHelper.loadingState.value)
    )
    val screenStateFlow: StateFlow<ScreenState> = screenState

    private var _errorToShowInSnackbar by mutableStateOf<Throwable?>(null)
    val errorToShowInSnackBar: Throwable? get() = _errorToShowInSnackbar

    init {
        refresh()
        listenToAuthState()
        observeScreenState()
    }

    private fun listenToAuthState() {
        logger.v { "listenToAuthState() called" }
        viewModelScope.launch {
            authRepo.isAuthorizedFlow.valueUpdatesOnly().collect {
                logger.d { "Authorization state changed to $it" }
                if (it) refresh()
            }
        }
    }

    private fun observeScreenState() {
        logger.v { "observeScreenState() called" }

        viewModelScope.launch {
            loadingHelper.loadingState.collect { loadingState ->
                screenState.update { oldScreenState ->
                    oldScreenState.copy(loadingState = loadingState)
                }
            }
        }

        viewModelScope.launch {
            newShoppingLists.collect { newLists ->
                screenState.update { oldScreenState ->
                    oldScreenState.copy(newLists = newLists)
                }
            }
        }
    }

    fun refresh() {
        logger.v { "refresh() called" }
        viewModelScope.launch {
            _errorToShowInSnackbar = loadingHelper.refresh().exceptionOrNull()
        }
    }

    fun onSnackbarShown() {
        logger.v { "onSnackbarShown() called" }
        _errorToShowInSnackbar = null
    }

    fun onAddShoppingListClicked() {
        logger.v { "onAddShoppingListClicked() called" }
    }

    fun onNewListNameChanged(index: Int, newName: String) {
        logger.v { "onNewListNameChanged($index, $newName) called" }
        newShoppingLists.update {
            it.toMutableList().apply {
                set(index, newName)
            }
        }
    }
}

data class ScreenState(
    val newLists: List<String> = emptyList(),
    val loadingState: LoadingState<List<GetShoppingListsSummaryResponse>>,
)
