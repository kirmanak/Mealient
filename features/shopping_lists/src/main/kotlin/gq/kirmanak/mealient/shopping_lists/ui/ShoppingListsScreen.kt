package gq.kirmanak.mealient.shopping_lists.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import gq.kirmanak.mealient.AppTheme
import gq.kirmanak.mealient.Dimens
import gq.kirmanak.mealient.datasource.models.ShoppingListInfo
import gq.kirmanak.mealient.shopping_list.R
import gq.kirmanak.mealient.shopping_lists.ui.composables.LazyColumnWithLoadingState
import gq.kirmanak.mealient.shopping_lists.ui.destinations.ShoppingListScreenDestination

@RootNavGraph(start = true)
@Destination(start = true)
@Composable
fun ShoppingListsScreen(
    navigator: DestinationsNavigator,
    shoppingListsViewModel: ShoppingListsViewModel = hiltViewModel(),
) {
    val loadingState = shoppingListsViewModel.loadingState.collectAsState()
    val errorToShowInSnackbar = shoppingListsViewModel.errorToShowInSnackBar

    LazyColumnWithLoadingState(
        loadingState = loadingState.value,
        errorToShowInSnackbar = errorToShowInSnackbar,
        onSnackbarShown = shoppingListsViewModel::onSnackbarShown,
        onRefresh = shoppingListsViewModel::refresh,
        defaultEmptyListError = stringResource(R.string.shopping_lists_screen_empty),
        lazyColumnContent = { items ->
            items(items) { shoppingList ->
                ShoppingListCard(
                    shoppingListEntity = shoppingList,
                    onItemClick = { clickedEntity ->
                        val shoppingListId = clickedEntity.id
                        navigator.navigate(ShoppingListScreenDestination(shoppingListId))
                    }
                )
            }
        }
    )
}

@Composable
@Preview
private fun PreviewShoppingListCard() {
    AppTheme {
        ShoppingListCard(shoppingListEntity = ShoppingListInfo("1", "Weekend shopping"))
    }
}

@Composable
private fun ShoppingListCard(
    shoppingListEntity: ShoppingListInfo?,
    modifier: Modifier = Modifier,
    onItemClick: (ShoppingListInfo) -> Unit = {},
) {
    Card(
        modifier = modifier
            .padding(horizontal = Dimens.Medium, vertical = Dimens.Small)
            .fillMaxWidth()
            .clickable { shoppingListEntity?.let { onItemClick(it) } },
    ) {
        Row(
            modifier = Modifier.padding(Dimens.Medium),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_shopping_cart),
                contentDescription = stringResource(id = R.string.shopping_lists_screen_cart_icon),
                modifier = Modifier.height(Dimens.Large),
            )
            Text(
                text = shoppingListEntity?.name.orEmpty(),
                modifier = Modifier.padding(start = Dimens.Medium),
            )
        }
    }
}

