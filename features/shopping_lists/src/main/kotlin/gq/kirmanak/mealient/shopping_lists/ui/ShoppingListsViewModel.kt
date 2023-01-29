package gq.kirmanak.mealient.shopping_lists.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.architecture.valueUpdatesOnly
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListEntity
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.shopping_lists.repo.ShoppingListsAuthRepo
import gq.kirmanak.mealient.shopping_lists.repo.ShoppingListsRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

@HiltViewModel
class ShoppingListsViewModel @Inject constructor(
    authRepo: ShoppingListsAuthRepo,
    logger: Logger,
    shoppingListsRepo: ShoppingListsRepo,
) : ViewModel() {

    val pages: Flow<PagingData<ShoppingListEntity>> =
        shoppingListsRepo.createPager().flow.cachedIn(viewModelScope)

    val needRetryFlow: Flow<Boolean> = authRepo.isAuthorizedFlow
        .valueUpdatesOnly()
        .onEach { logger.v { "Authorization state changed to $it" } }
        .shareIn(viewModelScope, started = SharingStarted.Eagerly, replay = 1)
}
