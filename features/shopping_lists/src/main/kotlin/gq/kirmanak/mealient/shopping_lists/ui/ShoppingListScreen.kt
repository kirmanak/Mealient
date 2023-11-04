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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.NoMeals
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import gq.kirmanak.mealient.AppTheme
import gq.kirmanak.mealient.Dimens
import gq.kirmanak.mealient.datasource.models.GetFoodResponse
import gq.kirmanak.mealient.datasource.models.GetShoppingListItemRecipeReferenceResponse
import gq.kirmanak.mealient.datasource.models.GetShoppingListItemResponse
import gq.kirmanak.mealient.datasource.models.GetUnitResponse
import gq.kirmanak.mealient.shopping_list.R
import gq.kirmanak.mealient.shopping_lists.ui.composables.LazyColumnWithLoadingState
import gq.kirmanak.mealient.shopping_lists.util.data
import gq.kirmanak.mealient.shopping_lists.util.map
import kotlinx.coroutines.android.awaitFrame
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
        contentPadding = PaddingValues(
            start = Dimens.Medium,
            end = Dimens.Medium,
            top = Dimens.Medium,
            bottom = Dimens.Large * 4,
        ),
        verticalArrangement = Arrangement.spacedBy(Dimens.Medium),
        defaultEmptyListError = defaultEmptyListError,
        errorToShowInSnackbar = shoppingListViewModel.errorToShowInSnackbar,
        onRefresh = shoppingListViewModel::refreshShoppingList,
        onSnackbarShown = shoppingListViewModel::onSnackbarShown,
        floatingActionButton = {
            FloatingActionButton(onClick = shoppingListViewModel::onAddItemClicked) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.shopping_list_screen_add_icon_content_description),
                )
            }
        }
    ) { items ->
        val firstCheckedItemIndex = items.indexOfFirst { it.checked }

        itemsIndexed(items, { _, item -> item.id }) { index, itemState ->
            if (itemState is ShoppingListItemState.ExistingItem) {
                if (itemState.isEditing) {
                    val state = remember {
                        ShoppingListItemEditorState(
                            state = itemState,
                            foods = loadingState.data?.foods.orEmpty(),
                            units = loadingState.data?.units.orEmpty(),
                        )
                    }
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
                        onCheckedChange = {
                            shoppingListViewModel.onItemCheckedChange(itemState, it)
                        },
                        onDismissed = { shoppingListViewModel.deleteShoppingListItem(itemState) },
                        onEditStart = { shoppingListViewModel.onEditStart(itemState) },
                    )
                }
            } else if (itemState is ShoppingListItemState.NewItem) {
                ShoppingListItemEditor(
                    state = itemState.item,
                    onEditCancelled = { shoppingListViewModel.onAddCancel(itemState) },
                    onEditConfirmed = { shoppingListViewModel.onAddConfirm(itemState) }
                )
            }
        }
    }
}

@Composable
fun ShoppingListItemEditor(
    state: ShoppingListItemEditorState,
    modifier: Modifier = Modifier,
    onEditCancelled: () -> Unit = {},
    onEditConfirmed: () -> Unit = {},
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Dimens.Small),
        horizontalAlignment = Alignment.End,
    ) {
        ShoppingListItemEditorFirstRow(
            state = state
        )
        if (state.isFood) {
            ShoppingListItemEditorFoodRow(state = state)
        }
        ShoppingListItemEditorButtonRow(
            state = state,
            onEditCancelled = onEditCancelled,
            onEditConfirmed = onEditConfirmed
        )
    }
}

@Composable
private fun ShoppingListItemEditorFirstRow(
    state: ShoppingListItemEditorState,
    modifier: Modifier = Modifier,
) {

    val focusRequester = remember { FocusRequester() }

    Row(
        modifier = modifier,
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
            modifier = Modifier
                .weight(3f, true)
                .focusRequester(focusRequester),
        )
    }

    LaunchedEffect(focusRequester) {
        awaitFrame()
        focusRequester.requestFocus()
    }
}

@Composable
private fun ShoppingListItemEditorButtonRow(
    state: ShoppingListItemEditorState,
    modifier: Modifier = Modifier,
    onEditCancelled: () -> Unit = {},
    onEditConfirmed: () -> Unit = {},
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Dimens.Small)
    ) {
        IconButton(onClick = {
            state.isFood = !state.isFood
        }) {
            val stringId = if (state.isFood) {
                R.string.shopping_list_screen_editor_not_food_button
            } else {
                R.string.shopping_list_screen_editor_food_button
            }
            val icon = if (state.isFood) {
                Icons.Default.NoMeals
            } else {
                Icons.Default.Restaurant
            }
            Icon(
                imageVector = icon,
                contentDescription = stringResource(id = stringId)
            )
        }

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ShoppingListItemEditorFoodRow(
    state: ShoppingListItemEditorState,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Dimens.Small),
    ) {
        ExposedDropdownMenuBox(
            expanded = state.foodsExpanded,
            onExpandedChange = { state.foodsExpanded = it },
            modifier = Modifier.weight(1f),
        ) {
            OutlinedTextField(
                value = state.food?.name.orEmpty(),
                onValueChange = { },
                modifier = Modifier.menuAnchor(),
                readOnly = true,
                textStyle = MaterialTheme.typography.bodyMedium,
                label = {
                    Text(
                        text = stringResource(id = R.string.shopping_list_screen_editor_food_label),
                        style = MaterialTheme.typography.labelMedium,
                    )
                },
                singleLine = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = state.foodsExpanded)
                },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            )

            ExposedDropdownMenu(
                expanded = state.foodsExpanded,
                onDismissRequest = { state.foodsExpanded = false }
            ) {
                state.foods.forEach {
                    DropdownMenuItem(
                        text = {
                            Text(text = it.name, style = MaterialTheme.typography.bodyMedium)
                        },
                        onClick = {
                            state.food = it
                            state.foodsExpanded = false
                        },
                        trailingIcon = {
                            if (it == state.food) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = stringResource(id = R.string.shopping_list_screen_editor_checked_unit_content_description),
                                )
                            }
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }

        ExposedDropdownMenuBox(
            expanded = state.unitsExpanded,
            onExpandedChange = { state.unitsExpanded = it },
            modifier = Modifier.weight(1f),
        ) {
            OutlinedTextField(
                value = state.unit?.name.orEmpty(),
                onValueChange = { },
                modifier = Modifier.menuAnchor(),
                readOnly = true,
                textStyle = MaterialTheme.typography.bodyMedium,
                label = {
                    Text(
                        text = stringResource(id = R.string.shopping_list_screen_editor_unit_label),
                        style = MaterialTheme.typography.labelMedium,
                    )
                },
                singleLine = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = state.unitsExpanded)
                },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            )

            ExposedDropdownMenu(
                expanded = state.unitsExpanded,
                onDismissRequest = { state.unitsExpanded = false }
            ) {
                state.units.forEach {
                    DropdownMenuItem(
                        text = {
                            Text(text = it.name, style = MaterialTheme.typography.bodyMedium)
                        },
                        onClick = {
                            state.unit = it
                            state.unitsExpanded = false
                        },
                        trailingIcon = {
                            if (it == state.unit) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = stringResource(id = R.string.shopping_list_screen_editor_checked_unit_content_description),
                                )
                            }
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }
    }
}

class ShoppingListItemEditorState(
    val foods: List<GetFoodResponse>,
    val units: List<GetUnitResponse>,
    val position: Int,
    val listId: String,
    note: String = "",
    quantity: String = "1.0",
    isFood: Boolean = false,
    food: GetFoodResponse? = null,
    unit: GetUnitResponse? = null,
) {

    constructor(
        state: ShoppingListItemState.ExistingItem,
        foods: List<GetFoodResponse>,
        units: List<GetUnitResponse>,
    ) : this(
        foods = foods,
        units = units,
        position = state.item.position,
        listId = state.item.shoppingListId,
        note = state.item.note,
        quantity = state.item.quantity.toString(),
        isFood = state.item.isFood,
        food = state.item.food,
        unit = state.item.unit,
    )

    var note: String by mutableStateOf(note)

    var quantity: String by mutableStateOf(quantity)

    var isFood: Boolean by mutableStateOf(isFood)

    var food: GetFoodResponse? by mutableStateOf(food)

    var unit: GetUnitResponse? by mutableStateOf(unit)

    var foodsExpanded: Boolean by mutableStateOf(false)

    var unitsExpanded: Boolean by mutableStateOf(false)
}

@Preview
@Composable
fun ShoppingListItemEditorPreview() {
    AppTheme {
        ShoppingListItemEditor(
            state = ShoppingListItemEditorState(
                state = ShoppingListItemState.ExistingItem(PreviewData.milk),
                foods = emptyList(),
                units = emptyList(),
            )
        )
    }
}

@Preview
@Composable
fun ShoppingListItemEditorNonFoodPreview() {
    AppTheme {
        ShoppingListItemEditor(
            state = ShoppingListItemEditorState(
                state = ShoppingListItemState.ExistingItem(PreviewData.blackTeaBags),
                foods = emptyList(),
                units = emptyList(),
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListItem(
    itemState: ShoppingListItemState.ExistingItem,
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
                        shoppingListItem.unit.takeIf { isFood }?.name,
                        shoppingListItem.food.takeIf { isFood }?.name,
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
            itemState = ShoppingListItemState.ExistingItem(PreviewData.milk),
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
            itemState = ShoppingListItemState.ExistingItem(PreviewData.blackTeaBags),
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
            itemState = ShoppingListItemState.ExistingItem(PreviewData.blackTeaBags),
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
            itemState = ShoppingListItemState.ExistingItem(PreviewData.blackTeaBags),
            showDivider = false,
            dismissState = rememberDismissState(
                initialValue = DismissValue.DismissedToEnd,
            ),
        )
    }
}

private object PreviewData {

    val blackTeaBags = GetShoppingListItemResponse(
        id = "1",
        shoppingListId = "1",
        checked = false,
        position = 0,
        isFood = false,
        note = "Black tea bags",
        quantity = 30.0,
        unit = null,
        food = null,
        recipeReferences = listOf(
            GetShoppingListItemRecipeReferenceResponse(
                recipeId = "1",
                recipeQuantity = 1.0,
            ),
        ),
    )

    val milk = GetShoppingListItemResponse(
        id = "2",
        shoppingListId = "1",
        checked = true,
        position = 0,
        isFood = true,
        note = "Cold",
        quantity = 500.0,
        unit = GetUnitResponse("ml", ""),
        food = GetFoodResponse("Milk", ""),
        recipeReferences = listOf(
            GetShoppingListItemRecipeReferenceResponse(
                recipeId = "1",
                recipeQuantity = 500.0,
            ),
        ),
    )
}