package gq.kirmanak.mealient.shopping_lists.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.architecture.valueUpdatesOnly
import gq.kirmanak.mealient.datasource.models.GetShoppingListsSummaryResponse
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.shopping_lists.repo.ShoppingListsAuthRepo
import gq.kirmanak.mealient.shopping_lists.repo.ShoppingListsRepo
import gq.kirmanak.mealient.shopping_lists.util.LoadingHelper
import gq.kirmanak.mealient.shopping_lists.util.LoadingHelperFactory
import gq.kirmanak.mealient.shopping_lists.util.LoadingState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingListsViewModel @Inject constructor(
    private val logger: Logger,
    private val shoppingListsRepo: ShoppingListsRepo,
    private val authRepo: ShoppingListsAuthRepo,
    loadingHelperFactory: LoadingHelperFactory,
) : ViewModel() {

    private val loadingHelper: LoadingHelper<List<GetShoppingListsSummaryResponse>> =
        loadingHelperFactory.create(viewModelScope) { shoppingListsRepo.getShoppingLists() }

    val loadingState: StateFlow<LoadingState<List<GetShoppingListsSummaryResponse>>> =
        loadingHelper.loadingState

    private var _errorToShowInSnackbar by mutableStateOf<Throwable?>(null)
    val errorToShowInSnackBar: Throwable? get() = _errorToShowInSnackbar

    init {
        refresh()
        listenToAuthState()
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
}
