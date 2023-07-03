package gq.kirmanak.mealient.shopping_lists.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
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
import gq.kirmanak.mealient.datasource.models.FullRecipeInfo
import gq.kirmanak.mealient.datasource.models.RecipeIngredientInfo
import gq.kirmanak.mealient.datasource.models.RecipeInstructionInfo
import gq.kirmanak.mealient.datasource.models.RecipeSettingsInfo
import gq.kirmanak.mealient.datasource.models.ShoppingListItemInfo
import gq.kirmanak.mealient.datasource.models.ShoppingListItemRecipeReferenceInfo
import gq.kirmanak.mealient.shopping_list.R
import gq.kirmanak.mealient.shopping_lists.ui.composables.LazyColumnWithLoadingState
import gq.kirmanak.mealient.shopping_lists.util.data
import gq.kirmanak.mealient.shopping_lists.util.map
import java.text.DecimalFormat

data class ShoppingListNavArgs(
    val shoppingListId: String,
)

@Destination(
    navArgsDelegate = ShoppingListNavArgs::class,
)
@Composable
internal fun ShoppingListScreen(
    shoppingListViewModel: ShoppingListViewModel = hiltViewModel(),
) {
    val loadingState = shoppingListViewModel.loadingState.collectAsState().value
    val defaultEmptyListError = stringResource(
        R.string.shopping_list_screen_empty_list,
        loadingState.data?.name.orEmpty()
    )

    LazyColumnWithLoadingState(
        loadingState = loadingState.map { it.items },
        defaultEmptyListError = defaultEmptyListError,
        errorToShowInSnackbar = shoppingListViewModel.errorToShowInSnackbar,
        onRefresh = shoppingListViewModel::refreshShoppingList,
        onSnackbarShown = shoppingListViewModel::onSnackbarShown,
        lazyColumnContent = { items ->
            val firstCheckedItemIndex = items.indexOfFirst { it.item.checked }

            itemsIndexed(items) { index, item ->
                ShoppingListItem(
                    shoppingListItem = item.item,
                    isDisabled = item.isDisabled,
                    showDivider = index == firstCheckedItemIndex && index != 0,
                ) { isChecked ->
                    shoppingListViewModel.onItemCheckedChange(item.item, isChecked)
                }
            }
        }
    )
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

            val isFood = shoppingListItem.isFood
            val quantity = shoppingListItem.quantity
                .takeUnless { it == 0.0 }
                .takeUnless { it == 1.0 && !isFood }
                ?.let { DecimalFormat.getInstance().format(it) }
            val text = listOfNotNull(
                quantity,
                shoppingListItem.unit.takeIf { isFood },
                shoppingListItem.food.takeIf { isFood },
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
}