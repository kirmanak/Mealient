package gq.kirmanak.mealient.shopping_lists.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import gq.kirmanak.mealient.AppTheme
import gq.kirmanak.mealient.Dimens
import gq.kirmanak.mealient.database.recipe.entity.RecipeEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeIngredientEntity
import gq.kirmanak.mealient.database.shopping_lists.entity.ItemRecipeReferenceWithRecipe
import gq.kirmanak.mealient.database.shopping_lists.entity.RecipeWithIngredients
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListEntity
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListItemEntity
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListItemRecipeReferenceEntity
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListItemWithRecipes
import gq.kirmanak.mealient.database.shopping_lists.entity.ShoppingListWithItems
import gq.kirmanak.mealient.datasource.NetworkError
import gq.kirmanak.mealient.shopping_list.R
import gq.kirmanak.mealient.ui.OperationUiState

data class ShoppingListNavArgs(
    val shoppingListId: String,
)

@Destination(
    navArgsDelegate = ShoppingListNavArgs::class,
)
@Composable
fun ShoppingListScreen(
    shoppingListsViewModel: ShoppingListViewModel = hiltViewModel(),
) {
    val screenState = shoppingListsViewModel.uiState.collectAsState()

    ShoppingListScreenContent(
        state = screenState.value,
        onItemChecked = shoppingListsViewModel::onItemChecked,
        onItemUnchecked = shoppingListsViewModel::onItemUnchecked,
    )
}

@Composable
fun ShoppingListScreenContent(
    state: OperationUiState<ShoppingListWithItems>,
    modifier: Modifier = Modifier,
    onItemChecked: (ShoppingListItemWithRecipes) -> Unit = {},
    onItemUnchecked: (ShoppingListItemWithRecipes) -> Unit = {},
) {
    when (state) {
        is OperationUiState.Progress,
        is OperationUiState.Initial -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }
        is OperationUiState.Failure -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = state.exception.message
                        ?: stringResource(R.string.shopping_list_screen_unknown_error)
                )
            }
        }
        is OperationUiState.Success -> {
            val shoppingListWithItems = state.value
            val items = shoppingListWithItems.shoppingListItems.sortedBy { it.item.checked }

            if (shoppingListWithItems.shoppingListItems.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(
                            R.string.shopping_list_screen_empty_list,
                            shoppingListWithItems.shoppingList.name
                        )
                    )
                }
            } else {
                Column(
                    modifier = modifier.fillMaxSize(),
                ) {
                    LazyColumn {
                        items(items) {
                            ShoppingListItem(it) { isChecked ->
                                if (isChecked) onItemUnchecked(it) else onItemChecked(it)
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
@Preview
fun PreviewShoppingListInfo() {
    AppTheme {
        ShoppingListScreenContent(state = OperationUiState.Success(PreviewData.shoppingList))
    }
}

@Composable
@Preview
fun PreviewShoppingListInfoError() {
    AppTheme {
        ShoppingListScreenContent(
            state = OperationUiState.Failure(
                NetworkError.Unauthorized(RuntimeException())
            )
        )
    }
}

@Composable
@Preview
fun PreviewShoppingListInfoInitial() {
    AppTheme {
        ShoppingListScreenContent(state = OperationUiState.Initial())
    }
}

@Composable
@Preview
fun PreviewShoppingListInfoProgress() {
    AppTheme {
        ShoppingListScreenContent(state = OperationUiState.Progress())
    }
}

@Composable
fun ShoppingListItem(
    shoppingListItem: ShoppingListItemWithRecipes,
    modifier: Modifier = Modifier,
    onCheckedChange: (Boolean) -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(Dimens.Medium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Checkbox(
            checked = shoppingListItem.item.checked,
            onCheckedChange = onCheckedChange,
        )

        Text(
            modifier = Modifier
                .padding(Dimens.Small),
            text = "${shoppingListItem.item.quantity} ${shoppingListItem.item.unit}",
        )

        Text(
            modifier = Modifier
                .padding(Dimens.Small),
            text = shoppingListItem.item.food,
        )

        Text(
            modifier = Modifier
                .padding(Dimens.Small),
            text = shoppingListItem.item.note,
        )
    }
}

@Composable
@Preview
fun PreviewShoppingListItemChecked() {
    AppTheme {
        ShoppingListItem(shoppingListItem = PreviewData.milk)
    }
}

@Composable
@Preview
fun PreviewShoppingListItemUnchecked() {
    AppTheme {
        ShoppingListItem(shoppingListItem = PreviewData.blackTeaBags)
    }
}

private object PreviewData {
    val teaWithMilkRecipe = RecipeWithIngredients(
        recipe = RecipeEntity(
            remoteId = "1",
            recipeYield = "1 serving",
            disableAmounts = false,
        ),
        ingredients = listOf(
            RecipeIngredientEntity(
                localId = 1,
                recipeId = "1",
                note = "Tea bag",
                food = "",
                unit = "",
                quantity = 1.0,
                title = "",
            ),
            RecipeIngredientEntity(
                localId = 1,
                recipeId = "1",
                note = "",
                food = "Milk",
                unit = "ml",
                quantity = 500.0,
                title = "",
            ),
        ),
    )

    val blackTeaBags = ShoppingListItemWithRecipes(
        item = ShoppingListItemEntity(
            remoteId = "1",
            shoppingListId = "1",
            checked = false,
            position = 0,
            isFood = false,
            note = "Black tea bags",
            quantity = 30.0,
            unit = "",
            food = "",
        ),
        recipes = listOf(
            ItemRecipeReferenceWithRecipe(
                reference = ShoppingListItemRecipeReferenceEntity(
                    shoppingListItemId = "1",
                    recipeId = "1",
                    quantity = 1.0,
                ),
                recipe = teaWithMilkRecipe,
            ),
        ),
    )

    val milk = ShoppingListItemWithRecipes(
        item = ShoppingListItemEntity(
            remoteId = "2",
            shoppingListId = "1",
            checked = true,
            position = 1,
            isFood = true,
            note = "Cold",
            quantity = 500.0,
            unit = "ml",
            food = "Milk",
        ),
        recipes = listOf(
            ItemRecipeReferenceWithRecipe(
                reference = ShoppingListItemRecipeReferenceEntity(
                    shoppingListItemId = "2",
                    recipeId = "1",
                    quantity = 500.0,
                ),
                recipe = teaWithMilkRecipe,
            ),
        ),
    )

    val shoppingList = ShoppingListWithItems(
        shoppingList = ShoppingListEntity(remoteId = "1", name = "Tea with milk"),
        shoppingListItems = listOf(blackTeaBags, milk),
    )
}