package gq.kirmanak.mealient.shopping_lists.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.database.recipe.entity.ShoppingListEntity
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.shopping_lists.repo.ShoppingListsRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ShoppingListsViewModel @Inject constructor(
    shoppingListsRepo: ShoppingListsRepo,
    private val logger: Logger,
) : ViewModel() {

    val pages: Flow<PagingData<ShoppingListEntity>> =
        shoppingListsRepo.createPager().flow.cachedIn(viewModelScope)

    fun onShoppingListClicked(listInfo: ShoppingListEntity) {
        logger.v { "onShoppingListClicked() called with: listInfo = $listInfo" }
    }
}
