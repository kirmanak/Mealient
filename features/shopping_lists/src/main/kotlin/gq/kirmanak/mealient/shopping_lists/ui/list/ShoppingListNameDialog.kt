package gq.kirmanak.mealient.shopping_lists.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import gq.kirmanak.mealient.shopping_list.R
import gq.kirmanak.mealient.shopping_lists.ui.composables.MealientTextField
import gq.kirmanak.mealient.ui.AppTheme
import gq.kirmanak.mealient.ui.Dimens
import gq.kirmanak.mealient.ui.preview.ColorSchemePreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ShoppingListNameDialog(
    onEvent: (ShoppingListsEvent) -> Unit,
    listName: String,
    onConfirm: ShoppingListsEvent,
    modifier: Modifier = Modifier,
    oldName: String? = null
) {
    BasicAlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onEvent(ShoppingListsEvent.DialogDismissed)
        }
    ) {
        ShoppingListNameDialogContent(
            listName = listName,
            onEvent = onEvent,
            oldName = oldName,
            onConfirm = onConfirm
        )
    }
}

@Composable
private fun ShoppingListNameDialogContent(
    listName: String,
    onEvent: (ShoppingListsEvent) -> Unit,
    oldName: String?,
    onConfirm: ShoppingListsEvent,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(Dimens.Small),
    ) {
        Column(
            modifier = Modifier
                .padding(Dimens.Medium),
            verticalArrangement = Arrangement.spacedBy(Dimens.Medium)
        ) {
            Text(
                text = if (oldName == null) {
                    stringResource(R.string.shopping_lists_dialog_add_new_title)
                } else {
                    stringResource(id = R.string.shopping_lists_dialog_edit_name_title, oldName)
                },
                style = MaterialTheme.typography.titleMedium
            )

            NameTextField(
                listName = listName,
                isEdit = oldName != null,
                onEvent = onEvent
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.Small, Alignment.End)
            ) {
                IconButton(
                    onClick = { onEvent(ShoppingListsEvent.DialogDismissed) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = stringResource(id = R.string.shopping_lists_dialog_cancel)
                    )
                }

                IconButton(
                    onClick = {
                        if (oldName == null) {
                            onEvent(onConfirm)
                        } else {
                            onEvent(onConfirm)
                        }
                    },
                    enabled = listName.isNotBlank()
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = stringResource(id = R.string.shopping_lists_dialog_confirm)
                    )
                }
            }
        }
    }
}

@Composable
private fun NameTextField(
    listName: String,
    isEdit: Boolean,
    onEvent: (ShoppingListsEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val onDone: KeyboardActionScope.() -> Unit = {
        onEvent(ShoppingListsEvent.NewListSaved(listName))
    }

    val onEdit: (newValue: String) -> Unit = { newValue ->
        if (isEdit) {
            onEvent(ShoppingListsEvent.EditListInput(newValue))
        } else {
            onEvent(ShoppingListsEvent.NewListInput(newValue))
        }
    }

    val trailingIcon: @Composable () -> Unit = {
        IconButton(
            onClick = { onEdit("") }
        ) {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = stringResource(id = R.string.shopping_lists_dialog_name_clear_input)
            )
        }
    }

    val isInputEmpty = listName.isBlank()

    MealientTextField(
        modifier = modifier
            .showKeyboard()
            .fillMaxWidth(),
        value = listName,
        onValueChange = { onEdit(it) },
        placeholder = {
            Text(
                text = stringResource(R.string.shopping_lists_screen_list_name_placeholder)
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = if (isInputEmpty) ImeAction.None else ImeAction.Done,
        ),
        keyboardActions = KeyboardActions(
            onDone = onDone.takeUnless { isInputEmpty },
        ),
        trailingIcon = trailingIcon.takeUnless { isInputEmpty }
    )
}

private fun Modifier.showKeyboard() = composed {
    val windowInfo = LocalWindowInfo.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(windowInfo) {
        snapshotFlow { windowInfo.isWindowFocused }.collect { isWindowFocused ->
            if (isWindowFocused) {
                focusRequester.requestFocus()
            }
        }
    }

    focusRequester(focusRequester)
}

@ColorSchemePreview
@Composable
private fun ShoppingListNameDialogEditPreview() {
    AppTheme {
        ShoppingListNameDialog(
            listName = "Test",
            onConfirm = ShoppingListsEvent.DialogDismissed,
            oldName = "Old test",
            onEvent = {}
        )
    }
}

@ColorSchemePreview
@Composable
private fun ShoppingListNameDialogNewPreview() {
    AppTheme {
        ShoppingListNameDialog(
            listName = "Test",
            onConfirm = ShoppingListsEvent.DialogDismissed,
            onEvent = {},
        )
    }
}
