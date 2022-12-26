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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingListViewModel @Inject constructor(
    private val shoppingListsRepo: ShoppingListsRepo,
    private val logger: Logger,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val args: ShoppingListNavArgs = ShoppingListScreenDestination.argsFrom(savedStateHandle)

    private val _shoppingList = MutableStateFlow<ShoppingListWithItems?>(null)
    val shoppingList: StateFlow<ShoppingListWithItems?> = _shoppingList.asStateFlow()

    init {
        loadShoppingList(args.shoppingListId)
    }

    private fun loadShoppingList(id: String) {
        logger.v { "loadShoppingList() called with: id = $id" }
        viewModelScope.launch {
            _shoppingList.value = runCatchingExceptCancel {
                shoppingListsRepo.requestShoppingList(id)
            }.fold(
                onSuccess = {
                    logger.d { "loadShoppingList() success: $it" }
                    it
                },
                onFailure = {
                    logger.e(it) { "loadShoppingList() failed" }
                    null
                }
            )
        }
    }
}