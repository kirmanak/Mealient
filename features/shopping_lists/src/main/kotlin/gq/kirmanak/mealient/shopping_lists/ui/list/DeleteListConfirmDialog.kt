package gq.kirmanak.mealient.shopping_lists.ui.list

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import gq.kirmanak.mealient.shopping_list.R
import gq.kirmanak.mealient.ui.AppTheme

@Composable
internal fun DeleteListConfirmDialog(
    onEvent: (ShoppingListsEvent) -> Unit,
    onConfirm: ShoppingListsEvent,
    listName: String,
) {
    AlertDialog(
        onDismissRequest = {
            onEvent(ShoppingListsEvent.DialogDismissed)
        },
        dismissButton = {
            IconButton(
                onClick = { onEvent(ShoppingListsEvent.DialogDismissed) }
            ) {
                Icon(
                    imageVector = Icons.Default.Cancel,
                    contentDescription = stringResource(id = R.string.shopping_lists_dialog_cancel)
                )
            }
        },
        confirmButton = {
            IconButton(
                onClick = { onEvent(onConfirm) },
                enabled = listName.isNotBlank()
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = stringResource(id = R.string.shopping_lists_dialog_confirm)
                )
            }
        },
        title = {
            Text(
                text = stringResource(
                    id = R.string.shopping_lists_dialog_delete_confirm_title,
                    listName
                )
            )
        },
        text = {
            Text(
                text = stringResource(id = R.string.shopping_lists_dialog_delete_confirm_text)
            )
        }
    )
}

@Preview
@Composable
private fun DeleteListConfirmDialogPreview() {
    AppTheme {
        DeleteListConfirmDialog(
            onEvent = {},
            onConfirm = ShoppingListsEvent.DialogDismissed,
            listName = "Test"
        )
    }
}