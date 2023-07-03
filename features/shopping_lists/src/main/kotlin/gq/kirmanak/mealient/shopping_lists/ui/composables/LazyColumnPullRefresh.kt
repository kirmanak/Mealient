package gq.kirmanak.mealient.shopping_lists.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun LazyColumnPullRefresh(
    modifier: Modifier = Modifier,
    refreshState: PullRefreshState,
    isRefreshing: Boolean,
    lazyColumnContent: LazyListScope.() -> Unit
) {
    Box(
        modifier = modifier.pullRefresh(refreshState),
    ) {
        LazyColumn(modifier = modifier, content = lazyColumnContent)

        PullRefreshIndicator(
            modifier = Modifier.align(Alignment.TopCenter),
            refreshing = isRefreshing,
            state = refreshState
        )
    }
}