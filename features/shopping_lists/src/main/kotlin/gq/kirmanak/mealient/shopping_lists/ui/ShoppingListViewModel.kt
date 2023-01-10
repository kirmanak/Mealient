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
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingListViewModel @Inject constructor(
    private val shoppingListsRepo: ShoppingListsRepo,
    private val logger: Logger,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val args: ShoppingListNavArgs = ShoppingListScreenDestination.argsFrom(savedStateHandle)

    private val _operationState: MutableStateFlow<OperationUiState<Unit>> =
        MutableStateFlow(OperationUiState.Initial())

    private val _shoppingListFromDb: Flow<ShoppingListWithItems?> =
        shoppingListsRepo.getShoppingListWithItems(args.shoppingListId)

    val uiState: StateFlow<OperationUiState<ShoppingListWithItems>> =
        _operationState.combine(_shoppingListFromDb, ::buildUiState)
            .stateIn(viewModelScope, SharingStarted.Eagerly, OperationUiState.Initial())


    init {
        refreshShoppingList()
    }

    private fun refreshShoppingList() {
        loadShoppingList(args.shoppingListId)
    }

    private fun loadShoppingList(id: String) {
        logger.v { "loadShoppingList() called with: id = $id" }
        viewModelScope.launch {
            _operationState.value = OperationUiState.Progress()
            val result = runCatchingExceptCancel {
                shoppingListsRepo.updateShoppingList(id)
            }
            _operationState.value = OperationUiState.fromResult(result)
        }
    }

    private fun buildUiState(
        operationState: OperationUiState<Unit>,
        shoppingList: ShoppingListWithItems?,
    ): OperationUiState<ShoppingListWithItems> {
        logger.v { "buildUiState() called with: operationState = $operationState, shoppingList = $shoppingList" }
        return when (operationState) {
            is OperationUiState.Failure -> OperationUiState.Failure(operationState.exception)
            is OperationUiState.Initial,
            is OperationUiState.Progress -> OperationUiState.Progress()
            is OperationUiState.Success -> {
                if (shoppingList == null) {
                    OperationUiState.Progress()
                } else {
                    OperationUiState.Success(shoppingList)
                }
            }
        }
    }

    fun onItemCheckedChange(item: ShoppingListItemWithRecipes, isChecked: Boolean) {
        logger.v { "onItemCheckedChange() called with: item = $item, isChecked = $isChecked" }
        viewModelScope.launch {
            val result = runCatchingExceptCancel {
                shoppingListsRepo.updateIsShoppingListItemChecked(item.item.remoteId, isChecked)
            }.onFailure {
                logger.e(it) { "Failed to update item's checked state" }
            }.onSuccess {
                logger.v { "Item's checked state updated" }
            }
            if (result.isSuccess) {
                refreshShoppingList()
            }
        }
    }
}