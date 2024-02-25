package gq.kirmanak.mealient.shopping_lists.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.navigate
import gq.kirmanak.mealient.shopping_list.R
import gq.kirmanak.mealient.shopping_lists.ui.composables.getErrorMessage
import gq.kirmanak.mealient.shopping_lists.ui.destinations.ShoppingListScreenDestination
import gq.kirmanak.mealient.ui.AppTheme
import gq.kirmanak.mealient.ui.Dimens
import gq.kirmanak.mealient.ui.components.BaseScreenState
import gq.kirmanak.mealient.ui.components.BaseScreenWithNavigation
import gq.kirmanak.mealient.ui.components.LazyColumnWithLoadingState
import gq.kirmanak.mealient.ui.preview.ColorSchemePreview
import gq.kirmanak.mealient.ui.util.error

@Destination
@Composable
internal fun ShoppingListsScreen(
    navController: NavController,
    baseScreenState: BaseScreenState,
    shoppingListsViewModel: ShoppingListsViewModel = hiltViewModel(),
) {
    val screenState = shoppingListsViewModel.shoppingListsState

    BaseScreenWithNavigation(
        baseScreenState = baseScreenState,
    ) { modifier ->
        LazyColumnWithLoadingState(
            modifier = modifier,
            loadingState = screenState.loadingState,
            emptyListError = screenState.loadingState.error?.let { getErrorMessage(it) }
                ?: stringResource(R.string.shopping_lists_screen_empty),
            retryButtonText = stringResource(id = R.string.shopping_lists_screen_empty_button_refresh),
            snackbarText = screenState.errorToShow?.let { getErrorMessage(error = it) },
            onSnackbarShown = { shoppingListsViewModel.onEvent(ShoppingListsEvent.SnackbarShown) },
            onRefresh = { shoppingListsViewModel.onEvent(ShoppingListsEvent.RefreshRequested) },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { shoppingListsViewModel.onEvent(ShoppingListsEvent.AddShoppingList) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(id = R.string.shopping_lists_screen_add_icon_content_description),
                    )
                }
            }
        ) { items ->
            items(items) { shoppingList ->
                ShoppingListCard(
                    listName = shoppingList.name,
                    isEditing = false,
                    onItemClick = {
                        val shoppingListId = shoppingList.id
                        navController.navigate(ShoppingListScreenDestination(shoppingListId))
                    }
                )
            }

            itemsIndexed(screenState.newLists) { index, newList ->
                ShoppingListCard(
                    listName = newList,
                    isEditing = true,
                    onNameChange = {
                        val event = ShoppingListsEvent.NewListNameChanged(index, it)
                        shoppingListsViewModel.onEvent(event)
                    },
                    onEditDone = {
                        val event = ShoppingListsEvent.NewListSaved(index)
                        shoppingListsViewModel.onEvent(event)
                    }
                )
            }
        }
    }
}

@Composable
private fun ShoppingListCard(
    listName: String?,
    isEditing: Boolean,
    modifier: Modifier = Modifier,
    onItemClick: (() -> Unit)? = null,
    onNameChange: (String) -> Unit = {},
    onEditDone: () -> Unit = {},
) {
    Card(
        modifier = modifier
            .padding(horizontal = Dimens.Medium, vertical = Dimens.Small)
            .fillMaxWidth()
            .then(if (onItemClick == null) Modifier else Modifier.clickable(onClick = onItemClick))
    ) {
        Row(
            modifier = Modifier.padding(Dimens.Medium),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = stringResource(id = R.string.shopping_lists_screen_cart_icon),
                modifier = Modifier.height(Dimens.Large),
            )

            if (isEditing) {
                NameTextField(
                    listName = listName,
                    onNameChange = onNameChange,
                    onEditDone = onEditDone,
                    modifier = Modifier.weight(1f),
                )

                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = stringResource(id = R.string.shopping_lists_screen_add_new_list_done_content_description),
                    tint = if (listName.isNullOrBlank()) {
                        LocalContentColor.current.copy(alpha = LocalContentColor.current.alpha / 2)
                    } else {
                        LocalContentColor.current
                    },
                    modifier = Modifier
                        .height(Dimens.Large)
                        .clickable(
                            enabled = listName
                                .isNullOrBlank()
                                .not(),
                            onClick = onEditDone,
                        ),
                )
            } else {
                Text(
                    text = listName.orEmpty(),
                    modifier = Modifier.padding(start = Dimens.Medium),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NameTextField(
    listName: String?,
    onNameChange: (String) -> Unit,
    onEditDone: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }

    BasicTextField(
        modifier = modifier.padding(start = Dimens.Medium),
        textStyle = LocalTextStyle.current,
        value = listName.orEmpty(),
        onValueChange = onNameChange,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = if (listName.isNullOrBlank()) ImeAction.None else ImeAction.Done,
        ),
        keyboardActions = KeyboardActions(
            onDone = { onEditDone() }
        ),
        interactionSource = interactionSource,
        decorationBox = @Composable { innerTextField ->
            TextFieldDefaults.DecorationBox(
                value = listName.orEmpty(),
                innerTextField = innerTextField,
                enabled = true,
                singleLine = true,
                visualTransformation = VisualTransformation.None,
                interactionSource = interactionSource,
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.shopping_lists_screen_add_new_list_placeholder),
                    )
                },
                contentPadding = PaddingValues(),
                container = {}
            )
        }
    )
}

@Composable
@ColorSchemePreview
private fun PreviewShoppingListCard() {
    AppTheme {
        ShoppingListCard(
            listName = "Weekend shopping",
            isEditing = false
        )
    }
}

@Composable
@ColorSchemePreview
private fun PreviewEditingShoppingListCard() {
    AppTheme {
        ShoppingListCard(
            listName = "Weekend shopping",
            isEditing = true
        )
    }
}

