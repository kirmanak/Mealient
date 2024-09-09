package gq.kirmanak.mealient.shopping_lists.ui.details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.NoMeals
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import gq.kirmanak.mealient.datasource.models.GetFoodResponse
import gq.kirmanak.mealient.datasource.models.GetShoppingListItemRecipeReferenceResponse
import gq.kirmanak.mealient.datasource.models.GetShoppingListItemResponse
import gq.kirmanak.mealient.datasource.models.GetUnitResponse
import gq.kirmanak.mealient.shopping_list.R
import gq.kirmanak.mealient.shopping_lists.ui.composables.EditableItemBox
import gq.kirmanak.mealient.shopping_lists.ui.composables.getErrorMessage
import gq.kirmanak.mealient.shopping_lists.util.ItemLabelGroup
import gq.kirmanak.mealient.ui.AppTheme
import gq.kirmanak.mealient.ui.Dimens
import gq.kirmanak.mealient.ui.components.BaseScreen
import gq.kirmanak.mealient.ui.components.LazyColumnWithLoadingState
import gq.kirmanak.mealient.ui.preview.ColorSchemePreview
import gq.kirmanak.mealient.ui.util.LoadingState
import gq.kirmanak.mealient.ui.util.data
import gq.kirmanak.mealient.ui.util.error
import gq.kirmanak.mealient.ui.util.map
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
    val loadingState by shoppingListViewModel.loadingState.collectAsState()

    BaseScreen { modifier ->
        ShoppingListScreen(
            modifier = modifier,
            loadingState = loadingState,
            errorToShowInSnackbar = shoppingListViewModel.errorToShowInSnackbar,
            onSnackbarShown = shoppingListViewModel::onSnackbarShown,
            onRefreshRequest = shoppingListViewModel::refreshShoppingList,
            onAddItemClicked = shoppingListViewModel::onAddItemClicked,
            onEditCancel = shoppingListViewModel::onEditCancel,
            onEditConfirm = shoppingListViewModel::onEditConfirm,
            onItemCheckedChange = shoppingListViewModel::onItemCheckedChange,
            onDeleteItem = shoppingListViewModel::deleteShoppingListItem,
            onEditStart = shoppingListViewModel::onEditStart,
            onAddCancel = shoppingListViewModel::onAddCancel,
            onAddConfirm = shoppingListViewModel::onAddConfirm,
        )
    }
}

@Composable
private fun ShoppingListScreen(
    loadingState: LoadingState<ShoppingListScreenState>,
    errorToShowInSnackbar: Throwable?,
    onSnackbarShown: () -> Unit,
    onRefreshRequest: () -> Unit,
    onAddItemClicked: () -> Unit,
    onEditCancel: (ShoppingListItemState.ExistingItem) -> Unit,
    onEditConfirm: (ShoppingListItemState.ExistingItem, ShoppingListItemEditorState) -> Unit,
    onItemCheckedChange: (ShoppingListItemState.ExistingItem, Boolean) -> Unit,
    onDeleteItem: (ShoppingListItemState.ExistingItem) -> Unit,
    onEditStart: (ShoppingListItemState.ExistingItem) -> Unit,
    onAddCancel: (ShoppingListItemState.NewItem) -> Unit,
    onAddConfirm: (ShoppingListItemState.NewItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    val defaultEmptyListError = stringResource(
        R.string.shopping_list_screen_empty_list,
        loadingState.data?.name.orEmpty()
    )

    var lastAddedItemIndex by remember { mutableIntStateOf(-1) }
    var showAddButton by remember { mutableStateOf(true) }
    val lazyListState = rememberLazyListState()
    LaunchedEffect(lastAddedItemIndex) {
        if (lastAddedItemIndex >= 0) lazyListState.animateScrollToItem(lastAddedItemIndex)
    }

    LazyColumnWithLoadingState(
        modifier = modifier,
        loadingState = loadingState.map { it.items },
        emptyListError = loadingState.error?.let { getErrorMessage(it) } ?: defaultEmptyListError,
        retryButtonText = stringResource(id = R.string.shopping_lists_screen_empty_button_refresh),
        contentPadding = PaddingValues(
            start = Dimens.Medium,
            end = Dimens.Medium,
            top = Dimens.Large,
            bottom = Dimens.Large,
        ),
        verticalArrangement = Arrangement.spacedBy(Dimens.Medium),
        snackbarText = errorToShowInSnackbar?.let { getErrorMessage(error = it) },
        onSnackbarShown = onSnackbarShown,
        onRefresh = onRefreshRequest,
        floatingActionButton = {
            // Only show the button if the editor is not active to avoid overlapping
            if (showAddButton) {
                FloatingActionButton(onClick = onAddItemClicked) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(id = R.string.shopping_list_screen_add_icon_content_description),
                    )
                }
            }
        },
        lazyListState = lazyListState
    ) { sortedItems ->

        lastAddedItemIndex = sortedItems.indexOfLast { it is ShoppingListItemState.NewItem }
        val firstCheckedItemIndex = sortedItems.indexOfFirst { it.checked }

        itemsIndexed(sortedItems, { _, item -> item.id}) { index, itemState ->
            when (itemState) {
                is ShoppingListItemState.ItemLabel -> {
                    ShoppingListSectionHeader(state = itemState)
                }
                is ShoppingListItemState.ExistingItem -> {
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
                            onEditCancelled = { onEditCancel(itemState) },
                            onEditConfirmed = { onEditConfirm(itemState, state) },
                            showAddButton = { showAddButton = it }
                        )
                    } else {
                        ShoppingListItem(
                            itemState = itemState,
                            showDivider = firstCheckedItemIndex == index,
                            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
                            onCheckedChange = { onItemCheckedChange(itemState, it) },
                            onDismissed = { onDeleteItem(itemState) },
                            onEditStart = { onEditStart(itemState) },
                        )
                    }
                }
                is ShoppingListItemState.NewItem -> {
                    ShoppingListItemEditor(
                        state = itemState.item,
                        onEditCancelled = { onAddCancel(itemState) },
                        onEditConfirmed = { onAddConfirm(itemState) },
                        showAddButton = { showAddButton = it }
                    )
                }
            }
        }
    }
}

@Composable
fun ShoppingListSectionHeader(state: ShoppingListItemState.ItemLabel) {
    // Skip displaying checked items group and otherwise display the label name
    val displayLabel = when (state.group) {
        is ItemLabelGroup.DefaultLabel -> stringResource(
            R.string.shopping_lists_screen_default_label)
        is ItemLabelGroup.Label -> state.group.label.name
        is ItemLabelGroup.CheckedItems -> return
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface),
    ) {
        Text(
            text = displayLabel,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(horizontal = Dimens.Small)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShoppingListItemEditor(
    state: ShoppingListItemEditorState,
    modifier: Modifier = Modifier,
    onEditCancelled: () -> Unit = {},
    onEditConfirmed: () -> Unit = {},
    showAddButton: (Boolean) -> Unit,
) {
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    var shouldBringIntoView by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .bringIntoViewRequester(bringIntoViewRequester),
        verticalArrangement = Arrangement.spacedBy(Dimens.Small),
        horizontalAlignment = Alignment.End,
    ) {
        ShoppingListItemEditorFirstRow(state = state)
        if (state.isFood) {
            ShoppingListItemEditorFoodRow(state = state)
        }
        ShoppingListItemEditorButtonRow(
            state = state,
            onEditCancelled = onEditCancelled,
            onEditConfirmed = onEditConfirmed,
            // Bring editor back into view when the user switches between food and non-food items
            onIconButtonClick = { shouldBringIntoView = true }
        )
    }


    LaunchedEffect (Unit, shouldBringIntoView) {
        showAddButton(false)
        bringIntoViewRequester.bringIntoView()
        shouldBringIntoView = false
    }
    DisposableEffect(Unit) {
        onDispose {
            // Show the add button again when the editor is dismissed
            showAddButton(true)
        }
    }
}

@Composable
private fun ShoppingListItemEditorFirstRow(
    state: ShoppingListItemEditorState,
    modifier: Modifier = Modifier,
) {

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
        )
    }
}

@Composable
private fun ShoppingListItemEditorButtonRow(
    state: ShoppingListItemEditorState,
    modifier: Modifier = Modifier,
    onEditCancelled: () -> Unit = {},
    onEditConfirmed: () -> Unit = {},
    onIconButtonClick: () -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Dimens.Small)
    ) {
        IconButton(onClick = {
            state.isFood = !state.isFood
            onIconButtonClick()
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
    var foodSearchQuery by remember { mutableStateOf(state.food?.name ?: "") }
    var unitSearchQuery by remember { mutableStateOf(state.unit?.name ?: "") }

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
                value = foodSearchQuery,
                onValueChange = {
                    foodSearchQuery = it
                    state.foodsExpanded = true },
                modifier = Modifier.menuAnchor(),
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

            val foodFilteringOptions = state.foods.filter {
                it.name.contains(foodSearchQuery, ignoreCase = true)
            }
            DropdownMenu (
                modifier = Modifier
                    .exposedDropdownSize(true)
                    .background(MaterialTheme.colorScheme.surface),
                properties = PopupProperties(focusable = false),
                expanded = state.foodsExpanded,
                onDismissRequest = { state.foodsExpanded = false }
            ) {
                foodFilteringOptions.forEach {
                    DropdownMenuItem(
                        text = {
                            Text(text = it.name, style = MaterialTheme.typography.bodyMedium)
                        },
                        onClick = {
                            state.food = it
                            state.foodsExpanded = false
                            foodSearchQuery = it.name
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
                value = unitSearchQuery,
                onValueChange = {
                    unitSearchQuery = it
                    state.unitsExpanded = true },
                modifier = Modifier.menuAnchor(),
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

            val unitFilteringOptions = state.units.filter {
                it.name.contains(unitSearchQuery, ignoreCase = true)
            }
            DropdownMenu (
                modifier = Modifier
                    .exposedDropdownSize(true)
                    .background(MaterialTheme.colorScheme.surface),
                properties = PopupProperties(focusable = false),
                expanded = state.unitsExpanded,
                onDismissRequest = { state.unitsExpanded = false }
            ) {
                unitFilteringOptions.forEach {
                    DropdownMenuItem(
                        text = {
                            Text(text = it.name, style = MaterialTheme.typography.bodyMedium)
                        },
                        onClick = {
                            state.unit = it
                            state.unitsExpanded = false
                            unitSearchQuery = it.name
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

@ColorSchemePreview
@Composable
fun ShoppingListItemEditorPreview() {
    AppTheme {
        ShoppingListItemEditor(
            state = ShoppingListItemEditorState(
                state = ShoppingListItemState.ExistingItem(PreviewData.milk),
                foods = emptyList(),
                units = emptyList(),
            ),
            showAddButton = {}
        )
    }
}

@ColorSchemePreview
@Composable
fun ShoppingListItemEditorNonFoodPreview() {
    AppTheme {
        ShoppingListItemEditor(
            state = ShoppingListItemEditorState(
                state = ShoppingListItemState.ExistingItem(PreviewData.blackTeaBags),
                foods = emptyList(),
                units = emptyList(),
            ),
            showAddButton = {}
        )
    }
}

@Composable
fun ShoppingListItem(
    itemState: ShoppingListItemState.ExistingItem,
    showDivider: Boolean,
    modifier: Modifier = Modifier,
    onCheckedChange: (Boolean) -> Unit = {},
    onDismissed: () -> Unit = {},
    onEditStart: () -> Unit = {},
) {
    EditableItemBox(
        modifier = modifier,
        onDelete = onDismissed,
        onEdit = onEditStart,
        deleteContentDescription = stringResource(R.string.shopping_list_screen_delete_icon_content_description),
        editContentDescription = stringResource(R.string.shopping_list_screen_edit_icon_content_description),
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface),
            ) {
                if (showDivider) {
                    HorizontalDivider()
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                ) {
                    Checkbox(
                        checked = itemState.item.checked,
                        onCheckedChange = onCheckedChange,
                    )

                    val shoppingListItem = itemState.item
                    val isFood = shoppingListItem.isFood
                    val quantity = shoppingListItem.quantity
                        .takeUnless { it == 0.0 }
                        .takeUnless { it == 1.0 && !isFood }
                        ?.let { DecimalFormat.getInstance().format(it) }

                    val primaryText = buildAnnotatedString {
                        fun appendWithSpace(text: String?) {
                            text?.let {
                                append(it)
                                append(" ")
                            }
                        }

                        fun appendBold(text: String?) {
                            text?.let {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append(it)
                                }
                            }
                        }

                        appendWithSpace(quantity)
                        if (!isFood) {
                            appendBold(shoppingListItem.note)
                        } else {
                            // Add plural unit name if available and quantity is greater than 1
                            shoppingListItem.unit?.let { unit ->
                                if (unit.pluralName.isNullOrEmpty() ||
                                    shoppingListItem.quantity <= 1) {
                                    appendWithSpace(unit.name)
                                } else {
                                    appendWithSpace(unit.pluralName)
                                }
                            }
                            appendBold(shoppingListItem.food?.name)
                        }
                    }

                    // only show note in secondary text if it's a food item due
                    // to the note already being displayed in the primary text otherwise
                    val secondaryText = shoppingListItem.takeIf { isFood }?.note.orEmpty()

                    Column {
                        Text(
                            text = primaryText,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        if (secondaryText.isNotBlank()) {
                            Text(
                                text = secondaryText,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    }
                }
            }
        },
    )
}

@Composable
@ColorSchemePreview
fun PreviewShoppingListItemChecked() {
    AppTheme {
        ShoppingListItem(
            itemState = ShoppingListItemState.ExistingItem(PreviewData.milk),
            showDivider = false
        )
    }
}

@Composable
@ColorSchemePreview
fun PreviewShoppingListItemUnchecked() {
    AppTheme {
        ShoppingListItem(
            itemState = ShoppingListItemState.ExistingItem(PreviewData.blackTeaBags),
            showDivider = true
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
        unit = GetUnitResponse(name= "ml", id= ""),
        food = GetFoodResponse("Milk", ""),
        recipeReferences = listOf(
            GetShoppingListItemRecipeReferenceResponse(
                recipeId = "1",
                recipeQuantity = 500.0,
            ),
        ),
    )
}