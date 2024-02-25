package gq.kirmanak.mealient.shopping_lists.ui.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.architecture.valueUpdatesOnly
import gq.kirmanak.mealient.datasource.models.CreateShoppingListRequest
import gq.kirmanak.mealient.datasource.models.GetShoppingListsSummaryResponse
import gq.kirmanak.mealient.datasource.runCatchingExceptCancel
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.shopping_lists.repo.ShoppingListsAuthRepo
import gq.kirmanak.mealient.shopping_lists.repo.ShoppingListsRepo
import gq.kirmanak.mealient.ui.util.LoadingHelper
import gq.kirmanak.mealient.ui.util.LoadingHelperFactory
import gq.kirmanak.mealient.ui.util.LoadingState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ShoppingListsViewModel @Inject constructor(
    private val logger: Logger,
    private val shoppingListsRepo: ShoppingListsRepo,
    private val authRepo: ShoppingListsAuthRepo,
    loadingHelperFactory: LoadingHelperFactory,
) : ViewModel() {

    private val loadingHelper: LoadingHelper<List<GetShoppingListsSummaryResponse>> =
        loadingHelperFactory.create(viewModelScope) {
            runCatchingExceptCancel { shoppingListsRepo.getShoppingLists() }
        }

    private var _shoppingListsState by mutableStateOf(
        ShoppingListsState(loadingState = loadingHelper.loadingState.value)
    )
    val shoppingListsState: ShoppingListsState get() = _shoppingListsState

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
                _shoppingListsState = _shoppingListsState.copy(loadingState = loadingState)
            }
        }
    }

    fun onEvent(event: ShoppingListsEvent) {
        logger.v { "onEvent($event) called" }
        when (event) {
            is ShoppingListsEvent.AddShoppingList -> {
                onAddShoppingListClicked()
            }

            is ShoppingListsEvent.NewListNameChanged -> {
                onNewListNameChanged(event.index, event.newName)
            }

            is ShoppingListsEvent.SnackbarShown -> {
                onSnackbarShown()
            }

            is ShoppingListsEvent.RefreshRequested -> {
                refresh()
            }

            is ShoppingListsEvent.NewListSaved -> {
                onNewListSaved(event.index)
            }
        }
    }

    private fun onNewListSaved(index: Int) {
        logger.v { "onNewListSaved($index) called" }
        val newLists = _shoppingListsState.newLists
        val request = CreateShoppingListRequest(name = newLists[index])
        viewModelScope.launch {
            runCatchingExceptCancel {
                shoppingListsRepo.addShoppingList(request)
            }.onFailure {
                logger.e(it) { "Error while creating shopping list" }
                _shoppingListsState = _shoppingListsState.copy(errorToShow = it)
            }.onSuccess {
                logger.d { "Shopping list \"${newLists[index]}\" created" }
                refresh()
                _shoppingListsState = _shoppingListsState.copy(
                    newLists = newLists.take(index) + newLists.drop(index + 1)
                )
            }
        }
    }

    private fun refresh() {
        logger.v { "refresh() called" }
        viewModelScope.launch {
            _shoppingListsState =
                _shoppingListsState.copy(errorToShow = loadingHelper.refresh().exceptionOrNull())
        }
    }

    private fun onSnackbarShown() {
        logger.v { "onSnackbarShown() called" }
        _shoppingListsState = _shoppingListsState.copy(errorToShow = null)
    }

    private fun onNewListNameChanged(index: Int, newName: String) {
        logger.v { "onNewListNameChanged($index, $newName) called" }
        val newLists = _shoppingListsState.newLists
        val filteredName = newName.replace(System.lineSeparator(), "")
        val withUpdatedItem = newLists.take(index) + filteredName + newLists.drop(index + 1)
        _shoppingListsState = _shoppingListsState.copy(newLists = withUpdatedItem)
    }

    private fun onAddShoppingListClicked() {
        logger.v { "onAddShoppingListClicked() called" }
        _shoppingListsState = _shoppingListsState.copy(newLists = _shoppingListsState.newLists + "")
    }
}

internal data class ShoppingListsState(
    val newLists: List<String> = emptyList(),
    val errorToShow: Throwable? = null,
    val loadingState: LoadingState<List<GetShoppingListsSummaryResponse>>,
)

internal sealed interface ShoppingListsEvent {

    data object AddShoppingList : ShoppingListsEvent

    data class NewListNameChanged(
        val index: Int,
        val newName: String,
    ) : ShoppingListsEvent

    data object SnackbarShown : ShoppingListsEvent

    data object RefreshRequested : ShoppingListsEvent

    data class NewListSaved(val index: Int) : ShoppingListsEvent
}