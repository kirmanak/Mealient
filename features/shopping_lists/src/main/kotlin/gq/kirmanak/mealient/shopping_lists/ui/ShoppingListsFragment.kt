package gq.kirmanak.mealient.shopping_lists.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealient.shopping_list.R
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
                ShoppingListsScreen()
            }
        }
    }
}

@Composable
fun ShoppingListsScreen(
    shoppingListsViewModel: ShoppingListsViewModel = viewModel()
) {
    val uiState by shoppingListsViewModel.uiState.collectAsState()

    ShoppingListsList(
        shoppingLists = uiState.shoppingLists,
        onItemClick = shoppingListsViewModel::onShoppingListClicked,
    )
}

@Preview
@Composable
fun PreviewShoppingListsList() {
    val list = (0 until 5).map { ShoppingListInfo("$it-th list", "$it") }
    ShoppingListsList(shoppingLists = list)
}

@Composable
private fun ShoppingListsList(
    shoppingLists: List<ShoppingListInfo>,
    modifier: Modifier = Modifier,
    onItemClick: (ShoppingListInfo) -> Unit = {},
) {
    MaterialTheme {
        LazyColumn(
            modifier = modifier.padding(Dimensions.Medium),
        ) {
            items(shoppingLists) {
                ShoppingListCard(shoppingListInfo = it, onItemClick = onItemClick)
            }
        }
    }
}


@Composable
@Preview
fun PreviewShoppingListCard() {
    ShoppingListCard(shoppingListInfo = ShoppingListInfo("Weekend shopping", "123"))
}

@Composable
fun ShoppingListCard(
    shoppingListInfo: ShoppingListInfo,
    modifier: Modifier = Modifier,
    onItemClick: (ShoppingListInfo) -> Unit = {},
) {
    Card(
        modifier = modifier
            .padding(Dimensions.Medium)
            .fillMaxWidth()
            .clickable { onItemClick(shoppingListInfo) },
    ) {
        Row(
            modifier = Modifier.padding(Dimensions.Medium),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_shopping_cart),
                contentDescription = stringResource(id = R.string.shopping_lists_screen_cart_icon),
                modifier = Modifier.height(Dimensions.Large),
            )
            Text(
                text = shoppingListInfo.name,
                modifier = Modifier.padding(start = Dimensions.Medium),
            )
        }
    }
}

object Dimensions {

    val Medium = 16.dp

    val Large = 24.dp
}