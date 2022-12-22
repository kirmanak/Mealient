package gq.kirmanak.mealient.shopping_lists.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealient.shopping_lists.network.ShoppingListInfo

@AndroidEntryPoint
class ShoppingListsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                ShoppingListsScreen(emptyList())
            }
        }
    }
}

@Preview
@Composable
fun PreviewShoppingListsScreen() {
    val list = (0 until 50).map { ShoppingListInfo("$it-th list", "$it") }
    ShoppingListsScreen(shoppingLists = list)
}

@Composable
fun ShoppingListsScreen(shoppingLists: List<ShoppingListInfo>) {
    LazyColumn {
        items(shoppingLists) {
            ShoppingListCard(shoppingListInfo = it)
        }
    }
}


@Composable
@Preview
fun PreviewShoppingListCard() {
    ShoppingListCard(shoppingListInfo = ShoppingListInfo("Weekend shopping", "123"))
}

@Composable
fun ShoppingListCard(shoppingListInfo: ShoppingListInfo) {
    Text(text = shoppingListInfo.name)
}