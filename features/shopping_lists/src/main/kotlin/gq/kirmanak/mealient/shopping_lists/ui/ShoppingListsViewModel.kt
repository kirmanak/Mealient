package gq.kirmanak.mealient.shopping_lists.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.datasource.runCatchingExceptCancel
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.shopping_lists.network.ShoppingListInfo
import gq.kirmanak.mealient.shopping_lists.network.ShoppingListsDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingListsViewModel @Inject constructor(
    private val shoppingListsDataSource: ShoppingListsDataSource,
    private val logger: Logger,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ShoppingListsUiState())
    val uiState: StateFlow<ShoppingListsUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        logger.v { "refresh() called" }
        viewModelScope.launch {
            val list = runCatchingExceptCancel {
                shoppingListsDataSource.getAll()
            }.onFailure {
                logger.e(it) { "refresh: can't get shopping lists" }
            }.getOrDefault(emptyList())
            _uiState.emit(ShoppingListsUiState(list))
        }
    }
}

data class ShoppingListsUiState(
    val shoppingLists: List<ShoppingListInfo> = emptyList(),
)