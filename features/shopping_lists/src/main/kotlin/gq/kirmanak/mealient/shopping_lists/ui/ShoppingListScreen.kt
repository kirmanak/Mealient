package gq.kirmanak.mealient.shopping_lists.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
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

@OptIn(ExperimentalMaterial3Api::class)
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
        contentPadding = PaddingValues(Dimens.Medium),
        verticalArrangement = Arrangement.spacedBy(Dimens.Medium),
        defaultEmptyListError = defaultEmptyListError,
        errorToShowInSnackbar = shoppingListViewModel.errorToShowInSnackbar,
        onRefresh = shoppingListViewModel::refreshShoppingList,
        onSnackbarShown = shoppingListViewModel::onSnackbarShown
    ) { items ->
        val firstCheckedItemIndex = items.indexOfFirst { it.item.checked }

        itemsIndexed(items, { _, item -> item.item.id }) { index, itemState ->
            if (itemState.isEditing) {
                val state = remember { ShoppingListEditorState(state = itemState) }
                ShoppingListItemEditor(
                    state = state,
                    onEditCancelled = { shoppingListViewModel.onEditCancel(itemState) },
                    onEditConfirmed = { shoppingListViewModel.onEditConfirm(itemState, state) }
                )
            } else {
                ShoppingListItem(
                    itemState = itemState,
                    showDivider = index == firstCheckedItemIndex && index != 0,
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface),
                    onCheckedChange = { shoppingListViewModel.onItemCheckedChange(itemState, it) },
                    onDismissed = { shoppingListViewModel.deleteShoppingListItem(itemState) },
                    onEditStart = { shoppingListViewModel.onEditStart(itemState) },
                )
            }
        }
    }
}

@Composable
fun ShoppingListItemEditor(
    state: ShoppingListEditorState,
    modifier: Modifier = Modifier,
    onEditCancelled: () -> Unit = {},
    onEditConfirmed: () -> Unit = {},
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Dimens.Small),
        horizontalAlignment = Alignment.End,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimens.Small),
        ) {
            OutlinedTextField(
                value = state.quantity,
                onValueChange = { newValue ->
                    val input = newValue.trim()
                        .let {
                            if (state.quantity == "0") {
                                it.removeSuffix("0").removePrefix("0")
                            } else {
                                it
                            }
                        }
                        .ifEmpty { "0" }
                    if (input.toDoubleOrNull() != null) {
                        state.quantity = input
                    }
                },
                modifier = Modifier.weight(1f),
                textStyle = MaterialTheme.typography.bodyMedium,
                label = {
                    Text(
                        text = stringResource(id = R.string.shopping_list_screen_editor_quantity_label),
                        style = MaterialTheme.typography.labelMedium,
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                ),
                singleLine = true,
            )

            OutlinedTextField(
                value = state.note,
                onValueChange = { state.note = it },
                textStyle = MaterialTheme.typography.bodyMedium,
                label = {
                    Text(
                        text = stringResource(id = R.string.shopping_list_screen_editor_note_label),
                        style = MaterialTheme.typography.labelMedium,
                    )
                },
                singleLine = true,
                modifier = Modifier.weight(3f, true),
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(Dimens.Small)) {
            IconButton(onClick = onEditCancelled) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(id = R.string.shopping_list_screen_editor_cancel_button)
                )
            }

            IconButton(onClick = onEditConfirmed) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = stringResource(id = R.string.shopping_list_screen_editor_save_button)
                )
            }
        }
    }
}

class ShoppingListEditorState(
    state: ShoppingListItemState,
) {

    var note: String by mutableStateOf(state.item.note)

    var quantity: String by mutableStateOf(state.item.quantity.toString())
}

@Preview
@Composable
fun ShoppingListItemEditorPreview() {
    AppTheme {
        ShoppingListItemEditor(
            state = ShoppingListEditorState(state = ShoppingListItemState(PreviewData.milk))
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListItem(
    itemState: ShoppingListItemState,
    showDivider: Boolean,
    modifier: Modifier = Modifier,
    onCheckedChange: (Boolean) -> Unit = {},
    onDismissed: () -> Unit = {},
    onEditStart: () -> Unit = {},
    dismissState: DismissState = rememberDismissState(
        confirmValueChange = {
            when (it) {
                DismissValue.DismissedToStart -> onDismissed()
                DismissValue.DismissedToEnd -> onEditStart()
                DismissValue.Default -> Unit
            }
            true
        }
    )
) {
    val shoppingListItem = itemState.item
    SwipeToDismiss(
        state = dismissState,
        background = {
            if (dismissState.targetValue == DismissValue.DismissedToStart) {
                val color by animateColorAsState(MaterialTheme.colorScheme.error)
                val iconColor by animateColorAsState(MaterialTheme.colorScheme.onError)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.shopping_list_screen_delete_icon_content_description),
                        tint = iconColor,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = Dimens.Small)
                    )
                }
            } else if (dismissState.targetValue == DismissValue.DismissedToEnd) {
                val color by animateColorAsState(MaterialTheme.colorScheme.primary)
                val iconColor by animateColorAsState(MaterialTheme.colorScheme.onPrimary)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(R.string.shopping_list_screen_edit_icon_content_description),
                        tint = iconColor,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = Dimens.Small)
                    )
                }
            }
        },
        dismissContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface),
            ) {
                if (showDivider) {
                    Divider()
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                ) {
                    Checkbox(
                        checked = itemState.item.checked,
                        onCheckedChange = onCheckedChange,
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
        },
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun PreviewShoppingListItemChecked() {
    AppTheme {
        ShoppingListItem(
            itemState = ShoppingListItemState(PreviewData.milk),
            showDivider = false
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun PreviewShoppingListItemUnchecked() {
    AppTheme {
        ShoppingListItem(
            itemState = ShoppingListItemState(PreviewData.blackTeaBags),
            showDivider = true
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun PreviewShoppingListItemDismissed() {
    AppTheme {
        ShoppingListItem(
            itemState = ShoppingListItemState(PreviewData.blackTeaBags),
            showDivider = false,
            dismissState = rememberDismissState(
                initialValue = DismissValue.DismissedToStart,
            ),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun PreviewShoppingListItemEditing() {
    AppTheme {
        ShoppingListItem(
            itemState = ShoppingListItemState(PreviewData.blackTeaBags),
            showDivider = false,
            dismissState = rememberDismissState(
                initialValue = DismissValue.DismissedToEnd,
            ),
        )
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