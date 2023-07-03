package gq.kirmanak.mealient.shopping_lists.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import gq.kirmanak.mealient.AppTheme
import gq.kirmanak.mealient.Dimens
import gq.kirmanak.mealient.datasource.NetworkError
import gq.kirmanak.mealient.datasource.models.FullRecipeInfo
import gq.kirmanak.mealient.datasource.models.FullShoppingListInfo
import gq.kirmanak.mealient.datasource.models.RecipeIngredientInfo
import gq.kirmanak.mealient.datasource.models.RecipeInstructionInfo
import gq.kirmanak.mealient.datasource.models.RecipeSettingsInfo
import gq.kirmanak.mealient.datasource.models.ShoppingListItemInfo
import gq.kirmanak.mealient.datasource.models.ShoppingListItemRecipeReferenceInfo
import gq.kirmanak.mealient.shopping_list.R
import gq.kirmanak.mealient.shopping_lists.ui.composables.CenteredProgressIndicator
import gq.kirmanak.mealient.shopping_lists.ui.composables.CenteredText
import gq.kirmanak.mealient.shopping_lists.util.LoadingState
import gq.kirmanak.mealient.shopping_lists.util.LoadingStateNoData
import gq.kirmanak.mealient.shopping_lists.util.LoadingStateWithData
import gq.kirmanak.mealient.shopping_lists.util.isRefreshing
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.DecimalFormat

data class ShoppingListNavArgs(
    val shoppingListId: String,
)

@Destination(
    navArgsDelegate = ShoppingListNavArgs::class,
)
@Composable
internal fun ShoppingListScreen(
    shoppingListsViewModel: ShoppingListViewModel = hiltViewModel(),
) {
    val screenState = shoppingListsViewModel.loadingState.collectAsState()

    ShoppingListScreenContent(
        state = screenState.value,
        onRefresh = shoppingListsViewModel::refreshShoppingList,
        onItemCheckedChange = shoppingListsViewModel::onItemCheckedChange,
        onSnackbarShown = shoppingListsViewModel::onSnackbarShown,
    )
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun ShoppingListScreenContent(
    state: LoadingState<ShoppingListScreenState>,
    modifier: Modifier = Modifier,
    onItemCheckedChange: (ShoppingListItemInfo, Boolean) -> Unit = { _, _ -> },
    onSnackbarShown: () -> Unit = {},
    onRefresh: () -> Unit = {},
) {
    val refreshState = rememberPullRefreshState(
        refreshing = state.isRefreshing,
        onRefresh = onRefresh,
    )
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = {
            val innerModifier = Modifier.padding(it)
            when (state) {
                is LoadingStateNoData.InitialLoad -> {
                    CenteredProgressIndicator(innerModifier)
                }

                is LoadingStateNoData.LoadError -> {
                    CenteredErrorMessage(state.error, innerModifier)
                }

                is LoadingStateWithData -> {
                    ShoppingListData(
                        screenState = state.data,
                        isRefreshing = state.isRefreshing,
                        refreshState = refreshState,
                        modifier = innerModifier,
                        onItemCheckedChange = onItemCheckedChange
                    )
                    ShortSnackbar(
                        snackbarState = state.data.snackbarState,
                        snackbarHostState = snackbarHostState,
                        scope = scope,
                        onSnackbarShown = onSnackbarShown
                    )
                }
            }
        },
    )
}

@Composable
private fun ShortSnackbar(
    snackbarState: SnackbarState,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    onSnackbarShown: () -> Unit = {},
) {

    val currentOnSnackbarShown by rememberUpdatedState(onSnackbarShown)

    when (snackbarState) {
        is SnackbarState.Hidden -> snackbarHostState.currentSnackbarData?.dismiss()
        is SnackbarState.Visible -> {
            LaunchedEffect(snackbarHostState) {
                scope.launch {
                    snackbarHostState.showSnackbar(message = snackbarState.message)
                    currentOnSnackbarShown()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ShoppingListData(
    screenState: ShoppingListScreenState,
    isRefreshing: Boolean,
    refreshState: PullRefreshState,
    modifier: Modifier,
    onItemCheckedChange: (ShoppingListItemInfo, Boolean) -> Unit
) {
    val shoppingListWithItems = screenState.shoppingList
    val disabledItems = screenState.disabledItems
    val items = shoppingListWithItems.items.sortedBy { it.checked }

    if (shoppingListWithItems.items.isEmpty()) {
        CenteredEmptyListText(shoppingListWithItems, modifier)
    } else {
        ShoppingListItemsColumn(
            items = items,
            disabledItems = disabledItems,
            isRefreshing = isRefreshing,
            refreshState = refreshState,
            modifier = modifier,
            onItemCheckedChange = onItemCheckedChange
        )
    }
}

@Composable
private fun CenteredEmptyListText(
    shoppingListWithItems: FullShoppingListInfo,
    modifier: Modifier = Modifier,
) {
    CenteredText(
        text = stringResource(
            R.string.shopping_list_screen_empty_list,
            shoppingListWithItems.name
        ),
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ShoppingListItemsColumn(
    items: List<ShoppingListItemInfo>,
    disabledItems: List<ShoppingListItemInfo>,
    isRefreshing: Boolean,
    refreshState: PullRefreshState,
    modifier: Modifier = Modifier,
    onItemCheckedChange: (ShoppingListItemInfo, Boolean) -> Unit
) {
    val firstCheckedItemIndex = items.indexOfFirst { it.checked }

    Box(
        modifier = modifier.pullRefresh(refreshState),
    ) {
        LazyColumn {
            itemsIndexed(items) { index, item ->
                ShoppingListItem(
                    shoppingListItem = item,
                    isDisabled = item in disabledItems,
                    showDivider = index == firstCheckedItemIndex && index != 0,
                ) { isChecked ->
                    onItemCheckedChange(item, isChecked)
                }
            }
        }

        PullRefreshIndicator(
            modifier = Modifier.align(Alignment.TopCenter),
            refreshing = isRefreshing,
            state = refreshState
        )
    }
}

@Composable
private fun CenteredErrorMessage(
    exception: Throwable,
    modifier: Modifier = Modifier,
) {
    CenteredText(
        text = exception.message ?: stringResource(R.string.shopping_list_screen_unknown_error),
        modifier = modifier,
    )
}

@Composable
@Preview
fun PreviewShoppingListInfo() {
    val state = LoadingStateWithData.Success(
        ShoppingListScreenState(
            shoppingList = PreviewData.shoppingList,
            disabledItems = listOf(PreviewData.blackTeaBags),
            snackbarState = SnackbarState.Hidden,
        )
    )
    AppTheme {
        ShoppingListScreenContent(state = state)
    }
}

@Composable
@Preview
fun PreviewShoppingListInfoError() {
    AppTheme {
        ShoppingListScreenContent(
            state = LoadingStateNoData.LoadError(
                NetworkError.Unauthorized(RuntimeException())
            )
        )
    }
}

@Composable
@Preview
fun PreviewShoppingListInfoInitial() {
    AppTheme {
        ShoppingListScreenContent(state = LoadingStateNoData.InitialLoad)
    }
}

@Composable
@Preview
fun PreviewShoppingListInfoProgress() {
    AppTheme {
        ShoppingListScreenContent(state = LoadingStateNoData.InitialLoad)
    }
}

@Composable
fun ShoppingListItem(
    shoppingListItem: ShoppingListItemInfo,
    isDisabled: Boolean,
    showDivider: Boolean,
    modifier: Modifier = Modifier,
    onCheckedChange: (Boolean) -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = Dimens.Small, end = Dimens.Small, start = Dimens.Small),
    ) {
        if (showDivider) {
            Divider()
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            Checkbox(
                checked = shoppingListItem.checked,
                onCheckedChange = onCheckedChange,
                enabled = !isDisabled,
            )

            val quantity = shoppingListItem.quantity
                .takeUnless { it == 0.0 }
                ?.let { DecimalFormat.getInstance().format(it) }
            val text = listOfNotNull(
                quantity,
                shoppingListItem.unit,
                shoppingListItem.food,
                shoppingListItem.note,
            ).filter { it.isNotBlank() }.joinToString(" ")

            Text(text = text)
        }
    }
}

@Composable
@Preview
fun PreviewShoppingListItemChecked() {
    AppTheme {
        ShoppingListItem(shoppingListItem = PreviewData.milk, false, false)
    }
}

@Composable
@Preview
fun PreviewShoppingListItemUnchecked() {
    AppTheme {
        ShoppingListItem(shoppingListItem = PreviewData.blackTeaBags, false, false)
    }
}

@Composable
@Preview
fun PreviewShoppingListItemCheckedDisabled() {
    AppTheme {
        ShoppingListItem(shoppingListItem = PreviewData.milk, true, false)
    }
}

@Composable
@Preview
fun PreviewShoppingListItemUncheckedDisabled() {
    AppTheme {
        ShoppingListItem(shoppingListItem = PreviewData.blackTeaBags, true, false)
    }
}

private object PreviewData {
    val teaWithMilkRecipe = FullRecipeInfo(
        remoteId = "1",
        name = "Tea with milk",
        recipeYield = "1 serving",
        recipeIngredients = listOf(
            RecipeIngredientInfo(
                note = "Tea bag",
                food = "",
                unit = "",
                quantity = 1.0,
                title = "",
            ),
            RecipeIngredientInfo(
                note = "",
                food = "Milk",
                unit = "ml",
                quantity = 500.0,
                title = "",
            ),
        ),
        recipeInstructions = listOf(
            RecipeInstructionInfo("Boil water"),
            RecipeInstructionInfo("Put tea bag in a cup"),
            RecipeInstructionInfo("Pour water into the cup"),
            RecipeInstructionInfo("Wait for 5 minutes"),
            RecipeInstructionInfo("Remove tea bag"),
            RecipeInstructionInfo("Add milk"),
        ),
        settings = RecipeSettingsInfo(
            disableAmounts = false
        ),
    )

    val blackTeaBags = ShoppingListItemInfo(
        id = "1",
        shoppingListId = "1",
        checked = false,
        position = 0,
        isFood = false,
        note = "Black tea bags",
        quantity = 30.0,
        unit = "",
        food = "",
        recipeReferences = listOf(
            ShoppingListItemRecipeReferenceInfo(
                shoppingListId = "1",
                id = "1",
                recipeId = "1",
                recipeQuantity = 1.0,
                recipe = teaWithMilkRecipe,
            ),
        ),
    )

    val milk = ShoppingListItemInfo(
        id = "2",
        shoppingListId = "1",
        checked = true,
        position = 1,
        isFood = true,
        note = "Cold",
        quantity = 500.0,
        unit = "ml",
        food = "Milk",
        recipeReferences = listOf(
            ShoppingListItemRecipeReferenceInfo(
                shoppingListId = "1",
                id = "2",
                recipeId = "1",
                recipeQuantity = 500.0,
                recipe = teaWithMilkRecipe,
            ),
        ),
    )

    val shoppingList = FullShoppingListInfo(
        id = "1",
        name = "Tea with milk",
        items = listOf(blackTeaBags, milk),
    )
}