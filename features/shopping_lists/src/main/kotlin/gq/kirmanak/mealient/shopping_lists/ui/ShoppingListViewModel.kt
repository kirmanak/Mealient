package gq.kirmanak.mealient.shopping_lists.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.architecture.valueUpdatesOnly
import gq.kirmanak.mealient.datasource.models.FullShoppingListInfo
import gq.kirmanak.mealient.datasource.models.ShoppingListItemInfo
import gq.kirmanak.mealient.datasource.runCatchingExceptCancel
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.shopping_lists.repo.ShoppingListsRepo
import gq.kirmanak.mealient.shopping_lists.ui.destinations.ShoppingListScreenDestination
import gq.kirmanak.mealient.shopping_lists.util.LoadingHelperFactory
import gq.kirmanak.mealient.shopping_lists.util.LoadingState
import gq.kirmanak.mealient.shopping_lists.util.LoadingStateNoData
import gq.kirmanak.mealient.shopping_lists.util.LoadingStateWithData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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

    private val _disabledItems: MutableStateFlow<List<ShoppingListItemInfo>> =
        MutableStateFlow(emptyList())

    private val _snackbarState: MutableStateFlow<SnackbarState> =
        MutableStateFlow(SnackbarState.Hidden)

    private val loadingHelper = loadingHelperFactory.create(viewModelScope) {
        shoppingListsRepo.getShoppingList(args.shoppingListId)
    }

    val loadingState: StateFlow<LoadingState<ShoppingListScreenState>> = combine(
        loadingHelper.loadingState,
        _disabledItems,
        _snackbarState,
        ::buildLoadingState,
    ).stateIn(viewModelScope, SharingStarted.Eagerly, LoadingStateNoData.InitialLoad)


    init {
        refreshShoppingList()
        showSnackBarOnRefreshError()
    }

    private fun showSnackBarOnRefreshError() {
        logger.v { "showSnackBarOnRefreshError() called" }
        loadingHelper.loadingState
            .valueUpdatesOnly()
            .filterIsInstance<LoadingStateWithData.RefreshError<FullShoppingListInfo>>()
            .onEach {
                val message = it.error.message
                if (message != null) {
                    _snackbarState.value = SnackbarState.Visible(message)
                }
            }.launchIn(viewModelScope)
    }

    fun refreshShoppingList() {
        logger.v { "refreshShoppingList() called" }
        loadingHelper.refresh()
    }

    private fun buildLoadingState(
        loadingState: LoadingState<FullShoppingListInfo>,
        disabledItems: List<ShoppingListItemInfo>,
        snackbarState: SnackbarState,
    ): LoadingState<ShoppingListScreenState> {
        logger.v { "buildLoadingState() called with: loadingState = $loadingState, disabledItems = $disabledItems, snackbarState = $snackbarState" }
        return when (loadingState) {
            is LoadingStateNoData.InitialLoad -> {
                LoadingStateNoData.InitialLoad
            }

            is LoadingStateNoData.LoadError -> {
                LoadingStateNoData.LoadError(loadingState.error)
            }

            is LoadingStateWithData.RefreshError -> {
                LoadingStateWithData.RefreshError(
                    ShoppingListScreenState(
                        shoppingList = loadingState.data,
                        disabledItems = disabledItems,
                        snackbarState = snackbarState,
                    ),
                    error = loadingState.error,
                )
            }

            is LoadingStateWithData.Refreshing -> {
                LoadingStateWithData.Refreshing(
                    ShoppingListScreenState(
                        shoppingList = loadingState.data,
                        disabledItems = disabledItems,
                        snackbarState = snackbarState,
                    ),
                )
            }

            is LoadingStateWithData.Success -> {
                LoadingStateWithData.Success(
                    ShoppingListScreenState(
                        shoppingList = loadingState.data,
                        disabledItems = disabledItems,
                        snackbarState = snackbarState,
                    ),
                )
            }
        }
    }

    fun onItemCheckedChange(item: ShoppingListItemInfo, isChecked: Boolean) {
        logger.v { "onItemCheckedChange() called with: item = $item, isChecked = $isChecked" }
        viewModelScope.launch {
            _disabledItems.update { it + item }
            val result = runCatchingExceptCancel {
                shoppingListsRepo.updateIsShoppingListItemChecked(item.id, isChecked)
            }
            _disabledItems.update { it - item }
            result.fold(
                onSuccess = {
                    logger.v { "Item's checked state updated" }
                    refreshShoppingList()
                },
                onFailure = { error ->
                    logger.e(error) { "Failed to update item's checked state" }
                    error.message?.let {
                        _snackbarState.value = SnackbarState.Visible(it)
                    }
                },
            )
        }
    }

    fun onSnackbarShown() {
        logger.v { "onSnackbarShown() called" }
        _snackbarState.value = SnackbarState.Hidden
    }
}