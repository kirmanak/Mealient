package gq.kirmanak.mealient.shopping_lists.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.navigate
import gq.kirmanak.mealient.shopping_list.R
import gq.kirmanak.mealient.shopping_lists.ui.composables.EditableItemBox
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
    val screenState by shoppingListsViewModel.shoppingListsState.collectAsState()

    ShoppingListsScreenDialog(
        dialog = screenState.dialog,
        onEvent = shoppingListsViewModel::onEvent
    )

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
            },
        ) { items ->
            items(
                items = items,
                key = { it.id },
                contentType = { "Existing list" }
            ) { displayList ->
                ShoppingListCard(
                    listName = displayList.name,
                    onClick = {
                        val shoppingListId = displayList.id
                        navController.navigate(ShoppingListScreenDestination(shoppingListId))
                    },
                    onDelete = {
                        shoppingListsViewModel.onEvent(ShoppingListsEvent.RemoveList(displayList))
                    },
                    onEdit = {
                        shoppingListsViewModel.onEvent(ShoppingListsEvent.EditList(displayList))
                    }
                )
            }
        }
    }
}

@Composable
private fun ShoppingListsScreenDialog(
    dialog: ShoppingListsDialog,
    onEvent: (ShoppingListsEvent) -> Unit,
) {
    when (dialog) {
        is ShoppingListsDialog.EditListItem -> {
            ShoppingListNameDialog(
                onEvent = onEvent,
                onConfirm = dialog.onConfirm,
                listName = dialog.listName,
                oldName = dialog.oldListName
            )

        }

        is ShoppingListsDialog.NewListItem -> {
            ShoppingListNameDialog(
                onEvent = onEvent,
                onConfirm = ShoppingListsEvent.NewListSaved(dialog.listName),
                listName = dialog.listName
            )
        }


        is ShoppingListsDialog.RemoveListItem -> {
            DeleteListConfirmDialog(
                onEvent = onEvent,
                onConfirm = dialog.onConfirm,
                listName = dialog.listName
            )
        }

        is ShoppingListsDialog.None -> {
            Unit
        }
    }
}

@Composable
private fun ShoppingListCard(
    listName: String,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    EditableItemBox(
        modifier = modifier,
        onDelete = onDelete,
        onEdit = onEdit,
        deleteContentDescription = stringResource(
            id = R.string.shopping_list_screen_delete_icon_content_description,
            listName
        ),
        editContentDescription = stringResource(
            id = R.string.shopping_list_screen_edit_icon_content_description,
            listName
        ),
        content = {
            Card(
                modifier = Modifier
                    .padding(
                        horizontal = Dimens.Medium,
                        vertical = Dimens.Small
                    )
                    .fillMaxWidth()
                    .clickable(
                        onClick = onClick
                    )
            ) {
                Row(
                    modifier = Modifier
                        .padding(Dimens.Medium),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Dimens.Medium)
                ) {
                    Icon(
                        modifier = Modifier
                            .height(Dimens.Large),
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = stringResource(id = R.string.shopping_lists_screen_cart_icon),
                    )


                    Text(
                        text = listName,
                    )
                }
            }
        },
    )
}

@Composable
@ColorSchemePreview
private fun PreviewShoppingListCard() {
    AppTheme {
        ShoppingListCard(
            listName = "Weekend shopping",
            onClick = {},
            onDelete = {},
            onEdit = {}
        )
    }
}

@Composable
@ColorSchemePreview
private fun PreviewEditingShoppingListCard() {
    AppTheme {
        ShoppingListCard(
            listName = "Weekend shopping",
            onClick = {},
            onDelete = {},
            onEdit = {}
        )
    }
}

