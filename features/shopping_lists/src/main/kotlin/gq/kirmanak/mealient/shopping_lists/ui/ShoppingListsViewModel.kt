package gq.kirmanak.mealient.shopping_lists.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListEntity
import gq.kirmanak.mealient.shopping_lists.repo.ShoppingListsRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ShoppingListsViewModel @Inject constructor(
    shoppingListsRepo: ShoppingListsRepo,
) : ViewModel() {

    val pages: Flow<PagingData<ShoppingListEntity>> =
        shoppingListsRepo.createPager().flow.cachedIn(viewModelScope)

}
