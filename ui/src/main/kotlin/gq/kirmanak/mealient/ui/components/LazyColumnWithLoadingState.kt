package gq.kirmanak.mealient.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import gq.kirmanak.mealient.ui.util.LoadingState
import gq.kirmanak.mealient.ui.util.LoadingStateNoData
import gq.kirmanak.mealient.ui.util.data
import gq.kirmanak.mealient.ui.util.isLoading
import gq.kirmanak.mealient.ui.util.isRefreshing

/**
 * `LazyColumnWithLoadingStateBase` is a private composable function that displays a `LazyColumn`
 * with different states based on the `loadingState`.
 * Function provides the core functionality, which can be extended or specialized by other functions.
 * In this case, it is used by LazyColumnWithLoadingStateForList and
 * LazyColumnWithLoadingStateForMap to handle specific data types (lists and maps, respectively).
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun <T> LazyColumnWithLoadingStateBase(
    loadingState: LoadingState<T>,
    emptyListError: String,
    retryButtonText: String,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    snackbarText: String?,
    onSnackbarShown: () -> Unit = {},
    onRefresh: () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    lazyListState: LazyListState = rememberLazyListState(),
    lazyColumnContent: LazyListScope.(T) -> Unit = {},
    isEmpty: (T) -> Boolean
) {
    val refreshState = rememberPullRefreshState(
        refreshing = loadingState.isRefreshing,
        onRefresh = onRefresh,
    )
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = modifier,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { paddingValues ->
        val innerModifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()

        val data = loadingState.data

        when {
            loadingState is LoadingStateNoData.InitialLoad -> {
                CenteredProgressIndicator(modifier = innerModifier)
            }

            !loadingState.isLoading && (data == null || isEmpty(data)) -> {
                EmptyListError(
                    text = emptyListError,
                    retryButtonText = retryButtonText,
                    onRetry = onRefresh,
                    modifier = innerModifier,
                )
            }

            else -> {
                LazyColumnPullRefresh(
                    refreshState = refreshState,
                    isRefreshing = loadingState.isRefreshing,
                    contentPadding = contentPadding,
                    verticalArrangement = verticalArrangement,
                    lazyColumnContent = { data?.let { lazyColumnContent(it) } ?: Unit },
                    lazyListState = lazyListState,
                    modifier = innerModifier,
                )

                ErrorSnackbar(
                    text = snackbarText,
                    snackbarHostState = snackbarHostState,
                    onSnackbarShown = onSnackbarShown,
                )
            }
        }
    }
}

/**
 * Function provides a specialized version of `LazyColumnWithLoadingStateBase` for lists.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T> LazyColumnWithLoadingStateForList(
    loadingState: LoadingState<List<T>>,
    emptyListError: String,
    retryButtonText: String,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    snackbarText: String?,
    onSnackbarShown: () -> Unit = {},
    onRefresh: () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    lazyListState: LazyListState = rememberLazyListState(),
    lazyColumnContent: LazyListScope.(List<T>) -> Unit = {},
) {
    LazyColumnWithLoadingStateBase(
        loadingState = loadingState,
        emptyListError = emptyListError,
        retryButtonText = retryButtonText,
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = verticalArrangement,
        snackbarText = snackbarText,
        onSnackbarShown = onSnackbarShown,
        onRefresh = onRefresh,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        lazyListState = lazyListState,
        lazyColumnContent = lazyColumnContent,
        isEmpty = { it.isEmpty() }
    )
}

/**
 * Function provides a specialized version of `LazyColumnWithLoadingStateBase` for maps.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <K, V> LazyColumnWithLoadingStateForMap(
    loadingState: LoadingState<Map<K, V>>,
    emptyListError: String,
    retryButtonText: String,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    snackbarText: String?,
    onSnackbarShown: () -> Unit = {},
    onRefresh: () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    lazyListState: LazyListState = rememberLazyListState(),
    lazyColumnContent: LazyListScope.(Map<K, V>) -> Unit = {},
) {
    LazyColumnWithLoadingStateBase(
        loadingState = loadingState,
        emptyListError = emptyListError,
        retryButtonText = retryButtonText,
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = verticalArrangement,
        snackbarText = snackbarText,
        onSnackbarShown = onSnackbarShown,
        onRefresh = onRefresh,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        lazyListState = lazyListState,
        lazyColumnContent = lazyColumnContent,
        isEmpty = { it.isEmpty() }
    )
}