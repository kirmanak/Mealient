package gq.kirmanak.mealient.shopping_lists.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.datasource.models.FullShoppingListInfo
import gq.kirmanak.mealient.datasource.models.ShoppingListItemInfo
import gq.kirmanak.mealient.datasource.runCatchingExceptCancel
import gq.kirmanak.mealient.logging.Logger
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
    loadingHelperFactory: LoadingHelperFactory,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val args: ShoppingListNavArgs = ShoppingListScreenDestination.argsFrom(savedStateHandle)

    private val _disabledItemIds: MutableStateFlow<Set<String>> = MutableStateFlow(mutableSetOf())

    private val loadingHelper = loadingHelperFactory.create(viewModelScope) {
        shoppingListsRepo.getShoppingList(args.shoppingListId)
    }

    val loadingState: StateFlow<LoadingState<ShoppingListScreenState>> = combine(
        loadingHelper.loadingState,
        _disabledItemIds,
        ::buildLoadingState,
    ).stateIn(viewModelScope, SharingStarted.Eagerly, LoadingStateNoData.InitialLoad)

    private var _errorToShowInSnackbar: Throwable? by mutableStateOf(null)
    val errorToShowInSnackbar: Throwable? get() = _errorToShowInSnackbar

    init {
        refreshShoppingList()
    }

    fun refreshShoppingList() {
        logger.v { "refreshShoppingList() called" }
        viewModelScope.launch {
            _errorToShowInSnackbar = loadingHelper.refresh().exceptionOrNull()
        }
    }

    private fun buildLoadingState(
        loadingState: LoadingState<FullShoppingListInfo>,
        disabledItemIds: Set<String>,
    ): LoadingState<ShoppingListScreenState> {
        logger.v { "buildLoadingState() called with: loadingState = $loadingState, disabledItems = $disabledItemIds" }
        return loadingState.map {
            val items = it.items.map { item ->
                ShoppingListItemState(item = item, isDisabled = item.id in disabledItemIds)
            }
            ShoppingListScreenState(name = it.name, items = items)
        }
    }

    fun onItemCheckedChange(item: ShoppingListItemInfo, isChecked: Boolean) {
        logger.v { "onItemCheckedChange() called with: item = $item, isChecked = $isChecked" }
        viewModelScope.launch {
            _disabledItemIds.update { it + item.id }
            val result = runCatchingExceptCancel {
                shoppingListsRepo.updateIsShoppingListItemChecked(item.id, isChecked)
            }.onFailure {
                logger.e(it) { "Failed to update item's checked state" }
            }
            _disabledItemIds.update { it - item.id }
            _errorToShowInSnackbar = result.exceptionOrNull()
            if (result.isSuccess) {
                logger.v { "Item's checked state updated" }
                refreshShoppingList()
            }
        }
    }

    fun onSnackbarShown() {
        logger.v { "onSnackbarShown() called" }
        _errorToShowInSnackbar = null
    }
}