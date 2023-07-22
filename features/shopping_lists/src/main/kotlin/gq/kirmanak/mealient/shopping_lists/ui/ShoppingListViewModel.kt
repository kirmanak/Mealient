package gq.kirmanak.mealient.shopping_lists.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.architecture.valueUpdatesOnly
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

    private val deletedItemIdsFlow = MutableStateFlow<Set<String>>(mutableSetOf())

    private val editingItemIdsFlow = MutableStateFlow<Set<String>>(mutableSetOf())

    private val editedItemsFlow = MutableStateFlow<MutableMap<String, ShoppingListItemInfo>>(
        mutableMapOf()
    )

    private val loadingHelper = loadingHelperFactory.create(viewModelScope) {
        loadShoppingListData()
    }

    val loadingState: StateFlow<LoadingState<ShoppingListScreenState>> = combine(
        loadingHelper.loadingState,
        deletedItemIdsFlow,
        editingItemIdsFlow,
        editedItemsFlow,
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
        deletedItemIds: Set<String>,
        editingItemIds: Set<String>,
        editedItems: Map<String, ShoppingListItemInfo>,
    ): LoadingState<ShoppingListScreenState> {
        logger.v { "buildLoadingState() called with: loadingState = $loadingState, deletedItemIds = $deletedItemIds, editingItemIds = $editingItemIds, editedItems = $editedItems" }
        return loadingState.map { data ->
            val items = data.shoppingList.items
                .filter { it.id !in deletedItemIds }
                .map {
                    ShoppingListItemState(
                        item = editedItems[it.id] ?: it,
                        isEditing = it.id in editingItemIds,
                    )
                }
                .sortedBy { it.item.checked }
            ShoppingListScreenState(
                name = data.shoppingList.name,
                items = items,
                foods = data.foods,
                units = data.units,
            )
        }
    }

    fun onItemCheckedChange(state: ShoppingListItemState, isChecked: Boolean) {
        logger.v { "onItemCheckedChange() called with: state = $state, isChecked = $isChecked" }
        val updatedItem = state.item.copy(checked = isChecked)
        updateItemInformation(updatedItem)
    }

    fun onSnackbarShown() {
        logger.v { "onSnackbarShown() called" }
        _errorToShowInSnackbar = null
    }

    fun deleteShoppingListItem(state: ShoppingListItemState) {
        logger.v { "deleteShoppingListItem() called with: state = $state" }
        val item = state.item
        viewModelScope.launch {
            deletedItemIdsFlow.update { it + item.id }
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
            deletedItemIdsFlow.update { it - item.id }
        }
    }

    fun onEditStart(state: ShoppingListItemState) {
        logger.v { "onEditStart() called with: state = $state" }
        val item = state.item
        editingItemIdsFlow.update { it + item.id }
    }

    fun onEditCancel(state: ShoppingListItemState) {
        logger.v { "onEditCancel() called with: state = $state" }
        val item = state.item
        editingItemIdsFlow.update { it - item.id }
    }

    fun onEditConfirm(state: ShoppingListItemState, input: ShoppingListEditorState) {
        logger.v { "onEditConfirm() called with: state = $state, input = $input" }
        val id = state.item.id
        editingItemIdsFlow.update { it - id }
        val updatedItem = state.item.copy(
            note = input.note,
            quantity = input.quantity.toDouble(),
        )
        updateItemInformation(updatedItem)
    }

    private fun updateItemInformation(updatedItem: ShoppingListItemInfo) {
        logger.v { "updateItemInformation() called with: updatedItem = $updatedItem" }
        val id = updatedItem.id
        viewModelScope.launch {
            editedItemsFlow.update { originalMap ->
                originalMap.toMutableMap().also { newMap -> newMap[id] = updatedItem }
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
            editedItemsFlow.update { originalMap ->
                originalMap.toMutableMap().also { newMap -> newMap.remove(id) }
            }
        }
    }
}