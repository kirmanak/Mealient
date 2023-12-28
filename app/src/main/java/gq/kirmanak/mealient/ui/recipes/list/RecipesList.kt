package gq.kirmanak.mealient.ui.recipes.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.navigate
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.ui.Dimens
import gq.kirmanak.mealient.ui.components.CenteredProgressIndicator
import gq.kirmanak.mealient.ui.components.LazyPagingColumnPullRefresh
import gq.kirmanak.mealient.ui.destinations.RecipeScreenDestination


@Destination
@Composable
internal fun RecipesList(
    navController: NavController,
    viewModel: RecipesListViewModel = hiltViewModel()
) {
    val state = viewModel.screenState.collectAsState()
    val stateValue = state.value

    LaunchedEffect(stateValue.recipeIdToOpen) {
        if (stateValue.recipeIdToOpen != null) {
            navController.navigate(RecipeScreenDestination(stateValue.recipeIdToOpen))
            viewModel.onEvent(RecipeListEvent.RecipeOpened)
        }
    }

    RecipesList(
        state = stateValue,
        onEvent = viewModel::onEvent,
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun RecipesList(
    state: RecipeListState,
    onEvent: (RecipeListEvent) -> Unit,
) {
    val recipes: LazyPagingItems<RecipeListItemState> =
        state.pagingDataRecipeState.collectAsLazyPagingItems()
    val isRefreshing = recipes.loadState.refresh is LoadState.Loading
    var itemToDelete: RecipeListItemState? by remember { mutableStateOf(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        state.snackbarState?.message?.let { message ->
            LaunchedEffect(message) {
                snackbarHostState.showSnackbar(message)
                onEvent(RecipeListEvent.SnackbarShown)
            }
        } ?: run {
            snackbarHostState.currentSnackbarData?.dismiss()
        }

        itemToDelete?.let { item ->
            ConfirmDeleteDialog(
                onDismissRequest = { itemToDelete = null },
                onConfirm = {
                    onEvent(RecipeListEvent.DeleteConfirmed(item))
                    itemToDelete = null
                },
                item = item,
            )
        }

        val innerModifier = Modifier
            .padding(padding)
            .consumeWindowInsets(padding)
        when {
            recipes.itemCount != 0 -> {
                RecipesListData(
                    modifier = innerModifier,
                    recipes = recipes,
                    onDeleteClick = { itemToDelete = it },
                    onFavoriteClick = { onEvent(RecipeListEvent.FavoriteClick(it)) },
                    onItemClick = { onEvent(RecipeListEvent.RecipeClick(it)) },
                )
            }

            isRefreshing -> {
                CenteredProgressIndicator(
                    modifier = innerModifier
                )
            }

            else -> {
                RecipesListError(
                    modifier = innerModifier,
                    recipes = recipes,
                )
            }
        }
    }
}

private val RecipeListSnackbar.message: String
    @Composable
    get() = when (this) {
        is RecipeListSnackbar.FavoriteAdded -> {
            stringResource(id = R.string.fragment_recipes_favorite_added, name)
        }

        is RecipeListSnackbar.FavoriteRemoved -> {
            stringResource(id = R.string.fragment_recipes_favorite_removed, name)
        }

        is RecipeListSnackbar.FavoriteUpdateFailed -> {
            stringResource(id = R.string.fragment_recipes_favorite_update_failed)
        }

        is RecipeListSnackbar.DeleteFailed -> {
            stringResource(id = R.string.fragment_recipes_delete_recipe_failed)
        }
    }

@Composable
private fun RecipesListData(
    modifier: Modifier,
    recipes: LazyPagingItems<RecipeListItemState>,
    onDeleteClick: (RecipeListItemState) -> Unit,
    onFavoriteClick: (RecipeListItemState) -> Unit,
    onItemClick: (RecipeListItemState) -> Unit
) {
    LazyPagingColumnPullRefresh(
        modifier = modifier
            .fillMaxSize(),
        lazyPagingItems = recipes,
        verticalArrangement = Arrangement.spacedBy(Dimens.Medium),
        contentPadding = PaddingValues(Dimens.Medium),
    ) {
        items(
            count = recipes.itemCount,
            key = recipes.itemKey { it.entity.remoteId },
            contentType = recipes.itemContentType { "recipe" },
        ) {
            val item: RecipeListItemState? = recipes[it]
            if (item != null) {
                RecipeItem(
                    modifier = Modifier
                        .fillMaxWidth(),
                    recipe = item,
                    onDeleteClick = { onDeleteClick(item) },
                    onFavoriteClick = { onFavoriteClick(item) },
                    onItemClick = { onItemClick(item) },
                )
            }
        }
    }
}

