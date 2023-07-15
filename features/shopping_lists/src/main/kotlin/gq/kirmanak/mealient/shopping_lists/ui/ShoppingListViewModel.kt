package gq.kirmanak.mealient.shopping_lists.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.architecture.valueUpdatesOnly
import gq.kirmanak.mealient.datasource.models.FullShoppingListInfo
import gq.kirmanak.mealient.datasource.models.ShoppingListItemInfo
import gq.kirmanak.mealient.datasource.runCatchingExceptCancel
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.shopping_lists.repo.ShoppingListsAuthRepo
import gq.kirmanak.mealient.shopping_lists.repo.ShoppingListsRepo
import gq.kirmanak.mealient.shopping_lists.ui.destinations.ShoppingListScreenDestination
import gq.kirmanak.mealient.shopping_lists.util.LoadingHelperFactory
import gq.kirmanak.mealient.shopping_lists.util.LoadingState
import gq.kirmanak.mealient.shopping_lists.util.LoadingStateNoData
import gq.kirmanak.mealient.shopping_lists.util.map
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ShoppingListViewModel @Inject constructor(
    private val shoppingListsRepo: ShoppingListsRepo,
    private val logger: Logger,
    private val authRepo: ShoppingListsAuthRepo,
    loadingHelperFactory: LoadingHelperFactory,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val args: ShoppingListNavArgs = ShoppingListScreenDestination.argsFrom(savedStateHandle)

    private val disabledItemIds: MutableStateFlow<Set<String>> = MutableStateFlow(mutableSetOf())

    private val deletedItemIds: MutableStateFlow<Set<String>> = MutableStateFlow(mutableSetOf())

    private val loadingHelper = loadingHelperFactory.create(viewModelScope) {
        shoppingListsRepo.getShoppingList(args.shoppingListId)
    }

    val loadingState: StateFlow<LoadingState<ShoppingListScreenState>> = combine(
        loadingHelper.loadingState,
        disabledItemIds,
        deletedItemIds,
        ::buildLoadingState,
    ).stateIn(viewModelScope, SharingStarted.Eagerly, LoadingStateNoData.InitialLoad)

    private var _errorToShowInSnackbar: Throwable? by mutableStateOf(null)
    val errorToShowInSnackbar: Throwable? get() = _errorToShowInSnackbar

    init {
        refreshShoppingList()
        listenToAuthState()
    }

    private fun listenToAuthState() {
        logger.v { "listenToAuthState() called" }
        viewModelScope.launch {
            authRepo.isAuthorizedFlow.valueUpdatesOnly().collect {
                logger.d { "Authorization state changed to $it" }
                if (it) doRefresh()
            }
        }
    }

    fun refreshShoppingList() {
        logger.v { "refreshShoppingList() called" }
        viewModelScope.launch {
            doRefresh()
        }
    }

    private suspend fun doRefresh() {
        _errorToShowInSnackbar = loadingHelper.refresh().exceptionOrNull()
    }

    private fun buildLoadingState(
        loadingState: LoadingState<FullShoppingListInfo>,
        disabledItemIds: Set<String>,
        deletedItemIds: Set<String>,
    ): LoadingState<ShoppingListScreenState> {
        logger.v { "buildLoadingState() called with: loadingState = $loadingState, disabledItems = $disabledItemIds" }
        return loadingState.map { shoppingList ->
            val items = shoppingList.items
                .filter { it.id !in deletedItemIds }
                .sortedBy { it.checked }
                .map { ShoppingListItemState(item = it, isDisabled = it.id in disabledItemIds) }
            ShoppingListScreenState(name = shoppingList.name, items = items)
        }
    }

    fun onItemCheckedChange(item: ShoppingListItemInfo, isChecked: Boolean) {
        logger.v { "onItemCheckedChange() called with: item = $item, isChecked = $isChecked" }
        viewModelScope.launch {
            disabledItemIds.update { it + item.id }
            val result = runCatchingExceptCancel {
                shoppingListsRepo.updateIsShoppingListItemChecked(item.id, isChecked)
            }.onFailure {
                logger.e(it) { "Failed to update item's checked state" }
            }
            _errorToShowInSnackbar = result.exceptionOrNull()
            if (result.isSuccess) {
                logger.v { "Item's checked state updated" }
                doRefresh()
            }
            disabledItemIds.update { it - item.id }
        }
    }

    fun onSnackbarShown() {
        logger.v { "onSnackbarShown() called" }
        _errorToShowInSnackbar = null
    }

    fun deleteShoppingListItem(item: ShoppingListItemInfo) {
        logger.v { "deleteShoppingListItem() called with: item = $item" }
        viewModelScope.launch {
            deletedItemIds.update { it + item.id }
            val result = runCatchingExceptCancel {
                shoppingListsRepo.deleteShoppingListItem(item.id)
            }.onFailure {
                logger.e(it) { "Failed to delete item" }
            }
            _errorToShowInSnackbar = result.exceptionOrNull()
            if (result.isSuccess) {
                logger.v { "Item deleted" }
                doRefresh()
            }
            deletedItemIds.update { it - item.id }
        }
    }
}