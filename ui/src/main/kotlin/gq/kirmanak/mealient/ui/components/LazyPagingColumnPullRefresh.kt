package gq.kirmanak.mealient.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T : Any> LazyPagingColumnPullRefresh(
    lazyPagingItems: LazyPagingItems<T>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    lazyColumnContent: LazyListScope.() -> Unit,
) {
    val isRefreshing = lazyPagingItems.loadState.refresh is LoadState.Loading

    val refreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = lazyPagingItems::refresh,
    )

    LazyColumnPullRefresh(
        modifier = modifier,
        refreshState = refreshState,
        isRefreshing = isRefreshing,
        contentPadding = contentPadding,
        verticalArrangement = verticalArrangement,
        lazyColumnContent = lazyColumnContent,
    )
}