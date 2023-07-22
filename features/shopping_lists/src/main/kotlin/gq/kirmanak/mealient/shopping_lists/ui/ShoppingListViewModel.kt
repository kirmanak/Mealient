package gq.kirmanak.mealient.shopping_lists.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.architecture.valueUpdatesOnly
import gq.kirmanak.mealient.datasource.models.NewShoppingListItemInfo
import gq.kirmanak.mealient.datasource.models.ShoppingListItemInfo
import gq.kirmanak.mealient.datasource.runCatchingExceptCancel
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.shopping_lists.repo.ShoppingListsAuthRepo
import gq.kirmanak.mealient.shopping_lists.repo.ShoppingListsRepo
import gq.kirmanak.mealient.shopping_lists.ui.destinations.ShoppingListScreenDestination
import gq.kirmanak.mealient.shopping_lists.util.LoadingHelperFactory
import gq.kirmanak.mealient.shopping_lists.util.LoadingState
import gq.kirmanak.mealient.shopping_lists.util.LoadingStateNoData
import gq.kirmanak.mealient.shopping_lists.util.data
import gq.kirmanak.mealient.shopping_lists.util.map
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
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

    private val editingStateFlow = MutableStateFlow(ShoppingListEditingState())

    private val loadingHelper = loadingHelperFactory.create(viewModelScope) {
        loadShoppingListData()
    }

    val loadingState: StateFlow<LoadingState<ShoppingListScreenState>> = combine(
        loadingHelper.loadingState,
        editingStateFlow,
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

    private suspend fun loadShoppingListData(): ShoppingListData = coroutineScope {
        val foodsDeferred = async {
            runCatchingExceptCancel {
                shoppingListsRepo.getFoods()
            }.getOrDefault(emptyList())
        }

        val unitsDeferred = async {
            runCatchingExceptCancel {
                shoppingListsRepo.getUnits()
            }.getOrDefault(emptyList())
        }

        val shoppingListDeferred = async {
            shoppingListsRepo.getShoppingList(args.shoppingListId)
        }

        ShoppingListData(
            foods = foodsDeferred.await(),
            units = unitsDeferred.await(),
            shoppingList = shoppingListDeferred.await(),
        )
    }

    private suspend fun doRefresh() {
        _errorToShowInSnackbar = loadingHelper.refresh().exceptionOrNull()
    }

    private fun buildLoadingState(
        loadingState: LoadingState<ShoppingListData>,
        editingState: ShoppingListEditingState,
    ): LoadingState<ShoppingListScreenState> {
        logger.v { "buildLoadingState() called with: loadingState = $loadingState, editingState = $editingState" }
        return loadingState.map { data ->
            val existingItems = data.shoppingList.items
                .filter { it.id !in editingState.deletedItemIds }
                .map {
                    ShoppingListItemState.ExistingItem(
                        item = editingState.modifiedItems[it.id] ?: it,
                        isEditing = it.id in editingState.editingItemIds,
                    )
                }
                .sortedBy { it.item.checked }
            ShoppingListScreenState(
                name = data.shoppingList.name,
                items = existingItems + editingState.newItems,
                foods = data.foods.sortedBy { it.name },
                units = data.units.sortedBy { it.name },
            )
        }
    }

    fun onItemCheckedChange(state: ShoppingListItemState.ExistingItem, isChecked: Boolean) {
        logger.v { "onItemCheckedChange() called with: state = $state, isChecked = $isChecked" }
        val updatedItem = state.item.copy(checked = isChecked)
        updateItemInformation(updatedItem)
    }

    fun onSnackbarShown() {
        logger.v { "onSnackbarShown() called" }
        _errorToShowInSnackbar = null
    }

    fun deleteShoppingListItem(state: ShoppingListItemState.ExistingItem) {
        logger.v { "deleteShoppingListItem() called with: state = $state" }
        val item = state.item
        viewModelScope.launch {
            editingStateFlow.update {
                it.copy(deletedItemIds = it.deletedItemIds + item.id)
            }
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
            editingStateFlow.update {
                it.copy(deletedItemIds = it.deletedItemIds - item.id)
            }
        }
    }

    fun onEditStart(state: ShoppingListItemState.ExistingItem) {
        logger.v { "onEditStart() called with: state = $state" }
        val item = state.item
        editingStateFlow.update {
            it.copy(editingItemIds = it.editingItemIds + item.id)
        }
    }

    fun onEditCancel(state: ShoppingListItemState.ExistingItem) {
        logger.v { "onEditCancel() called with: state = $state" }
        val item = state.item
        editingStateFlow.update {
            it.copy(editingItemIds = it.editingItemIds - item.id)
        }
    }

    fun onEditConfirm(
        state: ShoppingListItemState.ExistingItem,
        input: ShoppingListItemEditorState
    ) {
        logger.v { "onEditConfirm() called with: state = $state, input = $input" }
        val id = state.item.id
        editingStateFlow.update {
            it.copy(editingItemIds = it.editingItemIds - id)
        }
        val updatedItem = state.item.copy(
            note = input.note,
            quantity = input.quantity.toDouble(),
            isFood = input.isFood,
            unit = input.unit,
            food = input.food,
        )
        updateItemInformation(updatedItem)
    }

    private fun updateItemInformation(updatedItem: ShoppingListItemInfo) {
        logger.v { "updateItemInformation() called with: updatedItem = $updatedItem" }
        val id = updatedItem.id
        viewModelScope.launch {
            editingStateFlow.update { state ->
                state.copy(modifiedItems = state.modifiedItems + (id to updatedItem))
            }
            val result = runCatchingExceptCancel {
                shoppingListsRepo.updateShoppingListItem(updatedItem)
            }.onFailure {
                logger.e(it) { "Failed to update item" }
            }
            _errorToShowInSnackbar = result.exceptionOrNull()
            if (result.isSuccess) {
                logger.v { "Item updated" }
                doRefresh()
            }
            editingStateFlow.update { state ->
                state.copy(modifiedItems = state.modifiedItems - id)
            }
        }
    }

    fun onAddItemClicked() {
        logger.v { "onAddItemClicked() called" }
        val shoppingListData = loadingHelper.loadingState.value.data ?: return
        val maxPosition = shoppingListData.shoppingList.items.maxOfOrNull { it.position } ?: 0
        val editorState = ShoppingListItemEditorState(
            foods = shoppingListData.foods,
            units = shoppingListData.units,
            position = maxPosition + 1,
            listId = shoppingListData.shoppingList.id,
        )
        val item = ShoppingListItemState.NewItem(editorState)
        editingStateFlow.update {
            it.copy(newItems = it.newItems + item)
        }
    }

    fun onAddCancel(state: ShoppingListItemState.NewItem) {
        logger.v { "onAddCancel() called with: state = $state" }
        editingStateFlow.update {
            it.copy(newItems = it.newItems - state)
        }
    }

    fun onAddConfirm(
        state: ShoppingListItemState.NewItem,
    ) {
        logger.v { "onAddConfirm() called with: state = $state" }
        val item = state.item
        val newItem = NewShoppingListItemInfo(
            shoppingListId = item.listId,
            note = item.note,
            quantity = item.quantity.toDouble(),
            isFood = item.isFood,
            unit = item.unit,
            food = item.food,
            position = item.position,
        )
        viewModelScope.launch {
            val result = runCatchingExceptCancel {
                shoppingListsRepo.addShoppingListItem(newItem)
            }.onFailure {
                logger.e(it) { "Failed to add item" }
            }
            _errorToShowInSnackbar = result.exceptionOrNull()
            if (result.isSuccess) {
                logger.v { "Item added" }
                doRefresh()
            }
            editingStateFlow.update {
                it.copy(newItems = it.newItems - state)
            }
        }
    }
}