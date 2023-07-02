package gq.kirmanak.mealient.shopping_lists.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.architecture.valueUpdatesOnly
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.shopping_lists.repo.ShoppingListsAuthRepo
import gq.kirmanak.mealient.shopping_lists.repo.ShoppingListsRepo
import gq.kirmanak.mealient.shopping_lists.util.LoadingHelperFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingListsViewModel @Inject constructor(
    private val logger: Logger,
    private val shoppingListsRepo: ShoppingListsRepo,
    loadingHelperFactory: LoadingHelperFactory,
    authRepo: ShoppingListsAuthRepo,
) : ViewModel() {

    private val loadingHelper = loadingHelperFactory.create(viewModelScope) {
        shoppingListsRepo.getShoppingLists()
    }
    val loadingState = loadingHelper.loadingState

    init {
        refresh()
        listenToAuthState(authRepo)
    }

    private fun listenToAuthState(authRepo: ShoppingListsAuthRepo) {
        logger.v { "listenToAuthState() called" }
        viewModelScope.launch {
            authRepo.isAuthorizedFlow.valueUpdatesOnly().collect {
                logger.d { "Authorization state changed to $it" }
                if (it) refresh()
            }
        }
    }

    fun refresh() {
        logger.v { "refresh() called" }
        loadingHelper.refresh()
    }
}
