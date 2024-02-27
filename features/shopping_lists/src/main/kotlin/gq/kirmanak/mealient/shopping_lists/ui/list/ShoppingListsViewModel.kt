package gq.kirmanak.mealient.shopping_lists.ui.list

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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

    private var _shoppingListsState = MutableStateFlow(
        ShoppingListsState(loadingState = loadingHelper.loadingState.value)
    )
    val shoppingListsState: StateFlow<ShoppingListsState> get() = _shoppingListsState.asStateFlow()

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
                _shoppingListsState.update { it.copy(loadingState = loadingState) }
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
        val name = _shoppingListsState.value.newLists[index]
        val request = CreateShoppingListRequest(name = name)
        viewModelScope.launch {
            runCatchingExceptCancel {
                shoppingListsRepo.addShoppingList(request)
            }.onFailure { exception ->
                logger.e(exception) { "Error while creating shopping list" }
                _shoppingListsState.update { it.copy(errorToShow = exception) }
            }.onSuccess {
                logger.d { "Shopping list \"$name\" created" }
                refresh()
                _shoppingListsState.update {
                    it.copy(newLists = it.newLists.take(index) + it.newLists.drop(index + 1))
                }
            }
        }
    }

    private fun refresh() {
        logger.v { "refresh() called" }
        viewModelScope.launch {
            val errorToShow = loadingHelper.refresh().exceptionOrNull()
            _shoppingListsState.update { it.copy(errorToShow = errorToShow) }
        }
    }

    private fun onSnackbarShown() {
        logger.v { "onSnackbarShown() called" }
        _shoppingListsState.update { it.copy(errorToShow = null) }
    }

    private fun onNewListNameChanged(index: Int, newName: String) {
        logger.v { "onNewListNameChanged($index, $newName) called" }
        val filteredName = newName.replace(System.lineSeparator(), "")
        _shoppingListsState.update {
            val newLists = it.newLists.take(index) + filteredName + it.newLists.drop(index + 1)
            it.copy(newLists = newLists)
        }
    }

    private fun onAddShoppingListClicked() {
        logger.v { "onAddShoppingListClicked() called" }
        _shoppingListsState.update { it.copy(newLists = it.newLists + "") }
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