package gq.kirmanak.mealient.shopping_lists.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun ShoppingListScreen(
    shoppingListId: String,
    shoppingListsViewModel: ShoppingListsViewModel = hiltViewModel(),
) {

}