package gq.kirmanak.mealient.shopping_lists.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination

data class ShoppingListNavArgs(
    val shoppingListId: String,
)

@Destination(
    navArgsDelegate = ShoppingListNavArgs::class,
)
@Composable
fun ShoppingListScreen(
    shoppingListsViewModel: ShoppingListViewModel = hiltViewModel(),
) {
    val shoppingList = shoppingListsViewModel.shoppingList.collectAsState()

    Text(text = shoppingList.value?.shoppingList?.name ?: "Loading...")
}