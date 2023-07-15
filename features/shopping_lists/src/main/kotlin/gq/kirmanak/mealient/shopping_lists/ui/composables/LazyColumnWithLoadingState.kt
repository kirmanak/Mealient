package gq.kirmanak.mealient.shopping_lists.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import gq.kirmanak.mealient.shopping_lists.util.LoadingState
import gq.kirmanak.mealient.shopping_lists.util.LoadingStateNoData
import gq.kirmanak.mealient.shopping_lists.util.data
import gq.kirmanak.mealient.shopping_lists.util.error
import gq.kirmanak.mealient.shopping_lists.util.isLoading
import gq.kirmanak.mealient.shopping_lists.util.isRefreshing

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T> LazyColumnWithLoadingState(
    loadingState: LoadingState<List<T>>,
    defaultEmptyListError: String,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    errorToShowInSnackbar: Throwable? = null,
    onSnackbarShown: () -> Unit = {},
    onRefresh: () -> Unit = {},
    lazyColumnContent: LazyListScope.(List<T>) -> Unit = {},
) {
    val refreshState = rememberPullRefreshState(
        refreshing = loadingState.isRefreshing,
        onRefresh = onRefresh,
    )
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { paddingValues ->
        val innerModifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()

        val list = loadingState.data ?: emptyList()

        when {
            loadingState is LoadingStateNoData.InitialLoad -> {
                CenteredProgressIndicator(modifier = innerModifier)
            }

            !loadingState.isLoading && list.isEmpty() -> {
                EmptyListError(
                    loadError = loadingState.error,
                    onRetry = onRefresh,
                    defaultError = defaultEmptyListError,
                    modifier = innerModifier,
                )
            }

            else -> {
                LazyColumnPullRefresh(
                    refreshState = refreshState,
                    isRefreshing = loadingState.isRefreshing,
                    contentPadding = contentPadding,
                    verticalArrangement = verticalArrangement,
                    lazyColumnContent = { lazyColumnContent(list) },
                    modifier = innerModifier,
                )

                ErrorSnackbar(
                    error = errorToShowInSnackbar,
                    snackbarHostState = snackbarHostState,
                    onSnackbarShown = onSnackbarShown,
                )
            }
        }
    }
}

