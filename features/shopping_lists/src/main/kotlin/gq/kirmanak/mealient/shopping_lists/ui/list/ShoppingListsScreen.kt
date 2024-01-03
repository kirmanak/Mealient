package gq.kirmanak.mealient.shopping_lists.ui.list

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.navigate
import gq.kirmanak.mealient.datasource.models.GetShoppingListsSummaryResponse
import gq.kirmanak.mealient.shopping_list.R
import gq.kirmanak.mealient.shopping_lists.ui.composables.getErrorMessage
import gq.kirmanak.mealient.shopping_lists.ui.destinations.ShoppingListScreenDestination
import gq.kirmanak.mealient.ui.AppTheme
import gq.kirmanak.mealient.ui.Dimens
import gq.kirmanak.mealient.ui.components.BaseScreenState
import gq.kirmanak.mealient.ui.components.BaseScreenWithNavigation
import gq.kirmanak.mealient.ui.components.LazyColumnWithLoadingState
import gq.kirmanak.mealient.ui.preview.ColorSchemePreview
import gq.kirmanak.mealient.ui.util.error

@Destination
@Composable
fun ShoppingListsScreen(
    navController: NavController,
    baseScreenState: BaseScreenState,
    shoppingListsViewModel: ShoppingListsViewModel = hiltViewModel(),
) {
    val loadingState by shoppingListsViewModel.loadingState.collectAsState()
    val errorToShowInSnackbar = shoppingListsViewModel.errorToShowInSnackBar

    BaseScreenWithNavigation(
        baseScreenState = baseScreenState,
    ) { modifier ->
        LazyColumnWithLoadingState(
            modifier = modifier,
            loadingState = loadingState,
            emptyListError = loadingState.error?.let { getErrorMessage(it) }
                ?: stringResource(R.string.shopping_lists_screen_empty),
            retryButtonText = stringResource(id = R.string.shopping_lists_screen_empty_button_refresh),
            snackbarText = errorToShowInSnackbar?.let { getErrorMessage(error = it) },
            onSnackbarShown = shoppingListsViewModel::onSnackbarShown,
            onRefresh = shoppingListsViewModel::refresh
        ) { items ->
            items(items) { shoppingList ->
                ShoppingListCard(
                    shoppingList = shoppingList,
                    onItemClick = { clickedEntity ->
                        val shoppingListId = clickedEntity.id
                        navController.navigate(ShoppingListScreenDestination(shoppingListId))
                    }
                )
            }
        }
    }
}

@Composable
@ColorSchemePreview
private fun PreviewShoppingListCard() {
    AppTheme {
        ShoppingListCard(
            shoppingList = GetShoppingListsSummaryResponse("1", "Weekend shopping"),
        )
    }
}

@Composable
private fun ShoppingListCard(
    shoppingList: GetShoppingListsSummaryResponse?,
    modifier: Modifier = Modifier,
    onItemClick: (GetShoppingListsSummaryResponse) -> Unit = {},
) {
    Card(
        modifier = modifier
            .padding(horizontal = Dimens.Medium, vertical = Dimens.Small)
            .fillMaxWidth()
            .clickable { shoppingList?.let { onItemClick(it) } },
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
                text = shoppingList?.name.orEmpty(),
                modifier = Modifier.padding(start = Dimens.Medium),
            )
        }
    }
}

