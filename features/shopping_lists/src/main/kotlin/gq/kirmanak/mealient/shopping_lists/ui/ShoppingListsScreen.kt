package gq.kirmanak.mealient.shopping_lists.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import gq.kirmanak.mealient.AppTheme
import gq.kirmanak.mealient.Dimens
import gq.kirmanak.mealient.database.recipe.entity.ShoppingListEntity
import gq.kirmanak.mealient.shopping_list.R

@Composable
fun ShoppingListsScreen(
    shoppingListsViewModel: ShoppingListsViewModel = viewModel()
) {
    val items = shoppingListsViewModel.pages.collectAsLazyPagingItems()

    LazyPagingColumn(
        pagingItems = items,
    ) {
        ShoppingListCard(
            shoppingListEntity = it, onItemClick = shoppingListsViewModel::onShoppingListClicked
        )
    }
}

@Composable
private fun <T : Any> LazyPagingColumn(
    pagingItems: LazyPagingItems<T>,
    modifier: Modifier = Modifier,
    itemContent: @Composable LazyItemScope.(T?) -> Unit,
) {

    val loadStates = with(pagingItems.loadState) {
        listOfNotNull(
            refresh, prepend, append,
            source.prepend, source.append, source.refresh,
            mediator?.prepend, mediator?.append, mediator?.refresh,
        )
    }
    val isRefreshing = loadStates.any { it is LoadState.Loading }

    SwipeToRefresh(
        modifier = modifier, isRefreshing = isRefreshing, onRefresh = pagingItems::refresh
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
        ) {
            items(items = pagingItems, itemContent = itemContent)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SwipeToRefresh(
    modifier: Modifier = Modifier,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit,
) {
    val refreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = onRefresh,
    )

    Box(
        modifier = modifier.pullRefresh(state = refreshState)
    ) {
        content()

        PullRefreshIndicator(
            modifier = Modifier.align(Alignment.TopCenter),
            refreshing = isRefreshing,
            state = refreshState
        )
    }
}

@Composable
@Preview
private fun PreviewShoppingListCard() {
    AppTheme {
        ShoppingListCard(shoppingListEntity = ShoppingListEntity("1", "Weekend shopping"))
    }
}

@Composable
private fun ShoppingListCard(
    shoppingListEntity: ShoppingListEntity?,
    modifier: Modifier = Modifier,
    onItemClick: (ShoppingListEntity) -> Unit = {},
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