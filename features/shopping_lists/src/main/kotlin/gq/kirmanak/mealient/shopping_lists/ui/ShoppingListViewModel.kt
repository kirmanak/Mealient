package gq.kirmanak.mealient.shopping_lists.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.datasource.models.FullShoppingListInfo
import gq.kirmanak.mealient.datasource.runCatchingExceptCancel
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.shopping_lists.network.ShoppingListsDataSource
import gq.kirmanak.mealient.shopping_lists.ui.destinations.ShoppingListScreenDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingListViewModel @Inject constructor(
    private val shoppingListsDataSource: ShoppingListsDataSource,
    private val logger: Logger,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val args: ShoppingListNavArgs = ShoppingListScreenDestination.argsFrom(savedStateHandle)

    private val _shoppingList = MutableStateFlow<FullShoppingListInfo?>(null)
    val shoppingList: StateFlow<FullShoppingListInfo?> = _shoppingList.asStateFlow()

    init {
        loadShoppingList(args.shoppingListId)
    }

    private fun loadShoppingList(id: String) {
        logger.v { "loadShoppingList() called with: id = $id" }
        viewModelScope.launch {
            _shoppingList.value = runCatchingExceptCancel {
                shoppingListsDataSource.getShoppingList(id)
            }.onFailure {
                logger.e(it) { "Failed to load shopping list" }
            }.onSuccess {
                logger.d { "Loaded shopping list: $it" }
            }.getOrNull()
        }
    }
}