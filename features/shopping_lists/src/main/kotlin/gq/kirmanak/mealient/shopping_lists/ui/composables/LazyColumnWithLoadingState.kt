package gq.kirmanak.mealient.shopping_lists.ui.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import gq.kirmanak.mealient.shopping_lists.util.LoadingState
import gq.kirmanak.mealient.shopping_lists.util.LoadingStateNoData
import gq.kirmanak.mealient.shopping_lists.util.data
import gq.kirmanak.mealient.shopping_lists.util.error
import gq.kirmanak.mealient.shopping_lists.util.isLoading
import gq.kirmanak.mealient.shopping_lists.util.isRefreshing

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun <T> LazyColumnWithLoadingState(
    modifier: Modifier = Modifier,
    loadingState: LoadingState<List<T>>,
    errorToShowInSnackbar: Throwable?,
    onSnackbarShown: () -> Unit,
    defaultEmptyListError: String,
    onRefresh: () -> Unit,
    lazyColumnContent: LazyListScope.(List<T>) -> Unit,
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
                    modifier = innerModifier,
                    refreshState = refreshState,
                    isRefreshing = loadingState.isRefreshing,
                    lazyColumnContent = { lazyColumnContent(list) },
                )

                if (errorToShowInSnackbar == null) {
                    snackbarHostState.currentSnackbarData?.dismiss()
                } else {
                    ErrorSnackbar(
                        error = errorToShowInSnackbar,
                        snackbarHostState = snackbarHostState,
                        onSnackbarShown = onSnackbarShown,
                    )
                }
            }
        }
    }
}

