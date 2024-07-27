package gq.kirmanak.mealient.shopping_lists.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.architecture.valueUpdatesOnly
import gq.kirmanak.mealient.datasource.models.CreateShoppingListRequest
import gq.kirmanak.mealient.datasource.models.GetShoppingListsSummaryResponse
import gq.kirmanak.mealient.datasource.runCatchingExceptCancel
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.shopping_lists.repo.ShoppingListsAuthRepo
import gq.kirmanak.mealient.shopping_lists.repo.ShoppingListsRepo
import gq.kirmanak.mealient.ui.util.LoadingHelper
import gq.kirmanak.mealient.ui.util.LoadingHelperFactory
import gq.kirmanak.mealient.ui.util.LoadingState
import gq.kirmanak.mealient.ui.util.LoadingStateNoData
import gq.kirmanak.mealient.ui.util.map
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ShoppingListsViewModel @Inject constructor(
    private val logger: Logger,
    private val shoppingListsRepo: ShoppingListsRepo,
    private val authRepo: ShoppingListsAuthRepo,
    loadingHelperFactory: LoadingHelperFactory,
) : ViewModel() {

    private val loadingHelper: LoadingHelper<List<GetShoppingListsSummaryResponse>> =
        loadingHelperFactory.create(viewModelScope) {
            runCatchingExceptCancel { shoppingListsRepo.getShoppingLists() }
        }

    private var _shoppingListsState = MutableStateFlow(ShoppingListsState())
    val shoppingListsState: StateFlow<ShoppingListsState> get() = _shoppingListsState.asStateFlow()

    init {
        refresh()
        listenToAuthState()
        observeScreenState()
    }

    private fun listenToAuthState() {
        logger.v { "listenToAuthState() called" }
        viewModelScope.launch {
            authRepo.isAuthorizedFlow.valueUpdatesOnly().collect {
                logger.d { "Authorization state changed to $it" }
                if (it) refresh()
            }
        }
    }

    private fun observeScreenState() {
        logger.v { "observeScreenState() called" }

        loadingHelper.loadingState.onEach { loadingState ->
            logger.d { "screenStateUpdate: loadingState: $loadingState" }
            val existingLists: LoadingState<List<DisplayList>> = loadingState.map { lists ->
                lists.map { DisplayList(it) }
            }
            _shoppingListsState.update { it.copy(loadingState = existingLists) }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: ShoppingListsEvent) {
        logger.v { "onEvent($event) called" }
        when (event) {
            is ShoppingListsEvent.AddShoppingList -> {
                onAddShoppingListClicked()
            }

            is ShoppingListsEvent.NewListInput -> {
                onNewListInput(event)
            }

            is ShoppingListsEvent.SnackbarShown -> {
                onSnackbarShown()
            }

            is ShoppingListsEvent.RefreshRequested -> {
                refresh()
            }

            is ShoppingListsEvent.NewListSaved -> {
                onNewListSaved(event)
            }

            is ShoppingListsEvent.DialogDismissed -> {
                onDialogDismissed()
            }

            is ShoppingListsEvent.EditList -> {
                onEditList(event)
            }

            is ShoppingListsEvent.RemoveList -> {
                onRemoveList(event)
            }

            is ShoppingListsEvent.RemoveListConfirmed -> {
                onRemoveListConfirmed(event)
            }

            is ShoppingListsEvent.EditListConfirmed -> {
                onEditListConfirmed(event)
            }

            is ShoppingListsEvent.EditListInput -> {
                onEditListInput(event)
            }
        }
    }

    private fun onEditListConfirmed(event: ShoppingListsEvent.EditListConfirmed) {
        viewModelScope.launch {
            runCatchingExceptCancel {
                shoppingListsRepo.updateShoppingListName(
                    id = event.displayList.id,
                    name = event.listName
                )
            }.onFailure { exception ->
                logger.e(exception) { "Error while updating shopping list" }
                _shoppingListsState.update { it.copy(errorToShow = exception) }
            }.onSuccess {
                refresh()
                onDialogDismissed()
            }
        }
    }

    private fun onRemoveListConfirmed(event: ShoppingListsEvent.RemoveListConfirmed) {
        viewModelScope.launch {
            runCatchingExceptCancel {
                shoppingListsRepo.deleteShoppingList(event.displayList.id)
            }.onFailure { exception ->
                logger.e(exception) { "Error while deleting shopping list" }
                _shoppingListsState.update { it.copy(errorToShow = exception) }
            }.onSuccess {
                refresh()
                onDialogDismissed()
            }
        }
    }

    private fun onEditListInput(event: ShoppingListsEvent.EditListInput) {
        _shoppingListsState.update {
            val old = it.dialog as? ShoppingListsDialog.EditListItem ?: return@update it
            val onConfirm = old.onConfirm.copy(listName = event.newValue)
            it.copy(dialog = old.copy(listName = event.newValue, onConfirm = onConfirm))
        }
    }

    private fun onRemoveList(event: ShoppingListsEvent.RemoveList) {
        val onConfirm = ShoppingListsEvent.RemoveListConfirmed(
            displayList = event.list
        )
        _shoppingListsState.update {
            it.copy(dialog = ShoppingListsDialog.RemoveListItem(event.list.name, onConfirm))
        }
    }

    private fun onEditList(event: ShoppingListsEvent.EditList) {
        val name = event.list.name
        val onConfirm = ShoppingListsEvent.EditListConfirmed(
            listName = name,
            displayList = event.list
        )
        _shoppingListsState.update {
            it.copy(dialog = ShoppingListsDialog.EditListItem(name, name, onConfirm))
        }
    }

    private fun onDialogDismissed() {
        _shoppingListsState.update {
            it.copy(dialog = ShoppingListsDialog.None)
        }
    }

    private fun onNewListSaved(event: ShoppingListsEvent.NewListSaved) {
        logger.v { "onNewListSaved($event) called" }
        val request = CreateShoppingListRequest(event.name)
        viewModelScope.launch {
            runCatchingExceptCancel {
                shoppingListsRepo.addShoppingList(request)
            }.onFailure { exception ->
                logger.e(exception) { "Error while creating shopping list" }
                _shoppingListsState.update { it.copy(errorToShow = exception) }
            }.onSuccess {
                logger.d { "Shopping list \"${request.name}\" created" }
                refresh()
                onDialogDismissed()
            }
        }
    }

    private fun refresh() {
        logger.v { "refresh() called" }
        viewModelScope.launch {
            val errorToShow = loadingHelper.refresh().exceptionOrNull()
            _shoppingListsState.update {
                it.copy(errorToShow = errorToShow)
            }
        }
    }

    private fun onSnackbarShown() {
        logger.v { "onSnackbarShown() called" }
        _shoppingListsState.update {
            it.copy(errorToShow = null)
        }
    }

    private fun onNewListInput(event: ShoppingListsEvent.NewListInput) {
        logger.v { "onNewListNameChanged($event) called" }
        val filteredName = event.newName.replace(System.lineSeparator(), "")
        _shoppingListsState.update {
            it.copy(dialog = ShoppingListsDialog.NewListItem(filteredName))
        }
    }

    private fun onAddShoppingListClicked() {
        logger.v { "onAddShoppingListClicked() called" }
        _shoppingListsState.update {
            it.copy(dialog = ShoppingListsDialog.NewListItem(""))
        }
    }
}

internal data class DisplayList(
    private val list: GetShoppingListsSummaryResponse,
    val name: String = list.name.orEmpty(),
    val id: String = list.id
)

internal data class ShoppingListsState(
    val errorToShow: Throwable? = null,
    val loadingState: LoadingState<List<DisplayList>> = LoadingStateNoData.InitialLoad,
    val dialog: ShoppingListsDialog = ShoppingListsDialog.None
)

internal sealed interface ShoppingListsEvent {

    data object AddShoppingList : ShoppingListsEvent

    data class NewListInput(val newName: String) : ShoppingListsEvent

    data object SnackbarShown : ShoppingListsEvent

    data object RefreshRequested : ShoppingListsEvent

    data class NewListSaved(val name: String) : ShoppingListsEvent

    data object DialogDismissed : ShoppingListsEvent

    data class EditList(val list: DisplayList) : ShoppingListsEvent

    data class RemoveList(val list: DisplayList) : ShoppingListsEvent

    data class EditListInput(val newValue: String) : ShoppingListsEvent

    data class RemoveListConfirmed(
        val displayList: DisplayList,
    ) : ShoppingListsEvent

    data class EditListConfirmed(
        val listName: String,
        val displayList: DisplayList,
    ) : ShoppingListsEvent
}

internal sealed interface ShoppingListsDialog {

    data class NewListItem(val listName: String) : ShoppingListsDialog

    data class EditListItem(
        val listName: String,
        val oldListName: String,
        val onConfirm: ShoppingListsEvent.EditListConfirmed
    ) : ShoppingListsDialog

    data class RemoveListItem(
        val listName: String,
        val onConfirm: ShoppingListsEvent.RemoveListConfirmed
    ) : ShoppingListsDialog

    data object None : ShoppingListsDialog
}