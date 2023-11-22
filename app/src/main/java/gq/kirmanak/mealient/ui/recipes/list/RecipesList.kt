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
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import gq.kirmanak.mealient.ui.Dimens
import gq.kirmanak.mealient.ui.components.CenteredProgressIndicator
import gq.kirmanak.mealient.ui.components.LazyPagingColumnPullRefresh
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun RecipesList(
    recipesFlow: Flow<PagingData<RecipeListItemState>>,
    onDeleteClick: (RecipeListItemState) -> Unit,
    onFavoriteClick: (RecipeListItemState) -> Unit,
    onItemClick: (RecipeListItemState) -> Unit,
    onSnackbarShown: () -> Unit,
    snackbarMessageState: StateFlow<String?>,
) {
    val recipes: LazyPagingItems<RecipeListItemState> = recipesFlow.collectAsLazyPagingItems()
    val isRefreshing = recipes.loadState.refresh is LoadState.Loading
    var itemToDelete: RecipeListItemState? by remember { mutableStateOf(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarMessage: String? by snackbarMessageState.collectAsState()
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        snackbarMessage?.let { message ->
            LaunchedEffect(message) {
                snackbarHostState.showSnackbar(message)
                onSnackbarShown()
            }
        } ?: run {
            snackbarHostState.currentSnackbarData?.dismiss()
        }

        itemToDelete?.let { item ->
            ConfirmDeleteDialog(
                onDismissRequest = { itemToDelete = null },
                onConfirm = {
                    onDeleteClick(item)
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
                    onFavoriteClick = onFavoriteClick,
                    onItemClick = onItemClick
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

