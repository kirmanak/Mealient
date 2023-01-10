package gq.kirmanak.mealient.shopping_lists.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListWithItems
import gq.kirmanak.mealient.datasource.runCatchingExceptCancel
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.shopping_lists.repo.ShoppingListsRepo
import gq.kirmanak.mealient.shopping_lists.ui.destinations.ShoppingListScreenDestination
import gq.kirmanak.mealient.ui.OperationUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.zip
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

    private val _shoppingListFromDb: StateFlow<ShoppingListWithItems?> =
        shoppingListsRepo.getShoppingListWithItems(args.shoppingListId)
            .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val uiState: StateFlow<OperationUiState<ShoppingListWithItems>> =
        _operationState.zip(_shoppingListFromDb, ::buildUiState)
            .stateIn(viewModelScope, SharingStarted.Eagerly, OperationUiState.Initial())


    init {
        loadShoppingList(args.shoppingListId)
    }

    private fun loadShoppingList(id: String) {
        logger.v { "loadShoppingList() called with: id = $id" }
        viewModelScope.launch {
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
        return if (shoppingList == null) {
            operationState.exceptionOrNull
                ?.let { OperationUiState.Failure(it) }
                ?: OperationUiState.Progress()
        } else {
            OperationUiState.Success(shoppingList)
        }
    }
}