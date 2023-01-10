package gq.kirmanak.mealient.shopping_lists.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListItemWithRecipes
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListWithItems
import gq.kirmanak.mealient.datasource.runCatchingExceptCancel
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.shopping_lists.repo.ShoppingListsRepo
import gq.kirmanak.mealient.shopping_lists.ui.destinations.ShoppingListScreenDestination
import gq.kirmanak.mealient.ui.OperationUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ShoppingListViewModel @Inject constructor(
    private val shoppingListsRepo: ShoppingListsRepo,
    private val logger: Logger,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val args: ShoppingListNavArgs = ShoppingListScreenDestination.argsFrom(savedStateHandle)

    private val _operationState: MutableStateFlow<OperationUiState<Unit>> =
        MutableStateFlow(OperationUiState.Initial())

    private val _disabledItems: MutableStateFlow<List<ShoppingListItemWithRecipes>> =
        MutableStateFlow(emptyList())

    private val _snackbarState: MutableStateFlow<SnackbarState> =
        MutableStateFlow(SnackbarState.Hidden)

    private val _shoppingListFromDb: Flow<ShoppingListWithItems> =
        shoppingListsRepo.shoppingListWithItemsFlow(args.shoppingListId)

    val uiState: StateFlow<OperationUiState<ShoppingListScreenState>> = combine(
        _operationState,
        _shoppingListFromDb,
        _disabledItems,
        _snackbarState,
        ::buildUiState,
    ).stateIn(viewModelScope, SharingStarted.Eagerly, OperationUiState.Initial())


    init {
        refreshShoppingList()
    }

    private fun refreshShoppingList() {
        logger.v { "refreshShoppingList() called" }
        loadShoppingList(args.shoppingListId)
    }

    private fun loadShoppingList(id: String) {
        logger.v { "loadShoppingList() called with: id = $id" }
        viewModelScope.launch {
            val result = runCatchingExceptCancel {
                shoppingListsRepo.updateShoppingList(id)
            }
            if (result.isSuccess) {
                _shoppingListFromDb.first() // wait for the shopping list to be updated
            } else {
                result.exceptionOrNull()?.message?.let { message ->
                    _snackbarState.value = SnackbarState.Visible(message)
                }
            }
            _operationState.value = OperationUiState.fromResult(result)
        }
    }

    private fun buildUiState(
        operationState: OperationUiState<Unit>,
        shoppingList: ShoppingListWithItems,
        disabledItems: List<ShoppingListItemWithRecipes>,
        snackbarState: SnackbarState,
    ): OperationUiState<ShoppingListScreenState> {
        logger.v { "buildUiState() called with: operationState = $operationState, shoppingList = $shoppingList, disabledItems = $disabledItems" }
        return if (shoppingList.shoppingListItems.isNotEmpty()) {
            OperationUiState.Success(
                ShoppingListScreenState(
                    shoppingList = shoppingList,
                    disabledItems = disabledItems,
                    snackbarState = snackbarState,
                )
            )
        } else {
            when (operationState) {
                is OperationUiState.Failure -> OperationUiState.Failure(operationState.exception)
                is OperationUiState.Initial,
                is OperationUiState.Progress -> OperationUiState.Progress()
                is OperationUiState.Success -> {
                    OperationUiState.Success(
                        ShoppingListScreenState(
                            shoppingList = shoppingList,
                            disabledItems = disabledItems,
                            snackbarState = snackbarState,
                        )
                    )
                }
            }
        }
    }

    fun onItemCheckedChange(item: ShoppingListItemWithRecipes, isChecked: Boolean) {
        logger.v { "onItemCheckedChange() called with: item = $item, isChecked = $isChecked" }
        viewModelScope.launch {
            _disabledItems.update { it + item }
            val result = runCatchingExceptCancel {
                shoppingListsRepo.updateIsShoppingListItemChecked(item.item.remoteId, isChecked)
            }.onFailure {
                logger.e(it) { "Failed to update item's checked state" }
            }.onSuccess {
                logger.v { "Item's checked state updated" }
            }
            _disabledItems.update { it - item }
            if (result.isSuccess) {
                refreshShoppingList()
            } else {
                result.exceptionOrNull()?.message?.let {
                    _snackbarState.value = SnackbarState.Visible(it)
                }
            }
        }
    }

    fun onSnackbarShown() {
        logger.v { "onSnackbarShown() called" }
        _snackbarState.value = SnackbarState.Hidden
    }
}