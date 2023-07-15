package gq.kirmanak.mealient.shopping_lists.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
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
    refreshState: PullRefreshState,
    isRefreshing: Boolean,
    contentPadding: PaddingValues,
    verticalArrangement: Arrangement.Vertical,
    lazyColumnContent: LazyListScope.() -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.pullRefresh(refreshState),
    ) {
        LazyColumn(
            contentPadding = contentPadding,
            verticalArrangement = verticalArrangement,
            content = lazyColumnContent
        )

        PullRefreshIndicator(
            modifier = Modifier.align(Alignment.TopCenter),
            refreshing = isRefreshing,
            state = refreshState
        )
    }
}