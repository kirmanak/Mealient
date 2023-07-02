package gq.kirmanak.mealient.shopping_lists.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
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
import gq.kirmanak.mealient.datasource.NetworkError
import gq.kirmanak.mealient.datasource.models.ShoppingListInfo
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.shopping_list.R
import gq.kirmanak.mealient.shopping_lists.ui.composables.CenteredProgressIndicator
import gq.kirmanak.mealient.shopping_lists.ui.destinations.ShoppingListScreenDestination
import gq.kirmanak.mealient.shopping_lists.util.LoadingState
import gq.kirmanak.mealient.shopping_lists.util.LoadingStateNoData
import gq.kirmanak.mealient.shopping_lists.util.data
import gq.kirmanak.mealient.shopping_lists.util.error
import gq.kirmanak.mealient.shopping_lists.util.isLoading
import gq.kirmanak.mealient.shopping_lists.util.isRefreshing
import kotlinx.coroutines.launch

@RootNavGraph(start = true)
@Destination(start = true)
@Composable
fun ShoppingListsScreen(
    navigator: DestinationsNavigator,
    shoppingListsViewModel: ShoppingListsViewModel = hiltViewModel(),
) {
    val loadingState = shoppingListsViewModel.loadingState.collectAsState()

    ShoppingListScreenContent(
        loadingState = loadingState.value,
        onItemClick = { clickedEntity ->
            val shoppingListId = clickedEntity.id
            navigator.navigate(ShoppingListScreenDestination(shoppingListId))
        },
        onRefresh = {
            shoppingListsViewModel.refresh()
        }
    )
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun ShoppingListScreenContent(
    loadingState: LoadingState<List<ShoppingListInfo>>,
    onItemClick: (ShoppingListInfo) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val refreshState = rememberPullRefreshState(
        refreshing = loadingState.isRefreshing,
        onRefresh = onRefresh,
    )
    val snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { paddingValues ->
        val innerModifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()

        val list = loadingState.data ?: emptyList()
        val loadError = loadingState.error

        when {
            loadingState is LoadingStateNoData.InitialLoad -> {
                CenteredProgressIndicator(modifier = innerModifier)
            }

            !loadingState.isLoading && list.isEmpty() -> {
                EmptyListError(
                    loadError = loadError,
                    modifier = innerModifier,
                    onRetry = onRefresh,
                )
            }

            else -> {
                Box(
                    modifier = innerModifier.pullRefresh(refreshState),
                ) {
                    LazyShoppingListsColumn(items = list, onItemClick = onItemClick)

                    PullRefreshIndicator(
                        modifier = Modifier.align(Alignment.TopCenter),
                        refreshing = loadingState.isRefreshing,
                        state = refreshState
                    )
                }

                if (loadError == null) {
                    snackbarHostState.currentSnackbarData?.dismiss()
                } else {
                    ErrorSnackbar(error = loadError, snackbarHostState = snackbarHostState)
                }
            }
        }
    }
}

@Composable
private fun EmptyListError(
    loadError: Throwable?,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val text = loadError?.let { getErrorMessage(it) }
        ?: stringResource(R.string.shopping_lists_screen_empty)
    Box(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier.padding(top = Dimens.Medium),
                text = text,
            )
            Button(
                modifier = Modifier.padding(top = Dimens.Medium),
                onClick = onRetry,
            ) {
                Text(text = stringResource(id = R.string.shopping_lists_screen_empty_button_refresh))
            }
        }
    }
}

@Composable
@Preview
private fun PreviewEmptyListError() {
    AppTheme {
        EmptyListError(loadError = null, onRetry = {})
    }
}

@Composable
private fun LazyShoppingListsColumn(
    items: List<ShoppingListInfo>,
    onItemClick: (ShoppingListInfo) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        items(items) { shoppingList ->
            ShoppingListCard(
                shoppingListEntity = shoppingList,
                onItemClick = onItemClick,
            )
        }
    }
}

@Composable
private fun getErrorMessage(error: Throwable): String = when (error) {
    is NetworkError.Unauthorized -> stringResource(R.string.shopping_lists_screen_unauthorized_error)
    is NetworkError.NoServerConnection -> stringResource(R.string.shopping_lists_screen_no_connection)
    else -> error.message ?: stringResource(R.string.shopping_lists_screen_unknown_error)
}

@Composable
private fun ErrorSnackbar(
    error: Throwable,
    snackbarHostState: SnackbarHostState,
    logger: Logger = getComposeEntryPoint().provideLogger(),
) {
    logger.v { "ErrorSnackbar(error = $error)" }

    val lastShownSnackbarError = rememberSaveable { mutableStateOf<Throwable?>(null) }
    if (lastShownSnackbarError.value == error) {
        return
    } else {
        lastShownSnackbarError.value = error
    }

    val text = getErrorMessage(error = error)
    val scope = rememberCoroutineScope()

    LaunchedEffect(snackbarHostState) {
        scope.launch {
            snackbarHostState.showSnackbar(message = text)
        }
    }
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

