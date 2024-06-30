package gq.kirmanak.mealient.shopping_lists.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.input.VisualTransformation
import gq.kirmanak.mealient.shopping_list.R
import gq.kirmanak.mealient.ui.AppTheme
import gq.kirmanak.mealient.ui.Dimens
import gq.kirmanak.mealient.ui.preview.ColorSchemePreview
import kotlinx.coroutines.android.awaitFrame

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NewShoppingListDialog(
    onEvent: (ShoppingListsEvent) -> Unit,
    listName: String?,
    modifier: Modifier = Modifier
) {
    if (listName == null) {
        return
    }

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = {
            onEvent(ShoppingListsEvent.NewListDialogDismissed)
        }
    ) {
        NewShoppingListDialogContent(
            listName = listName,
            onEvent = onEvent
        )
    }
}

@Composable
private fun NewShoppingListDialogContent(
    listName: String,
    onEvent: (ShoppingListsEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .padding(horizontal = Dimens.Medium, vertical = Dimens.Small)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(Dimens.Medium),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier
                    .height(Dimens.Large),
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = stringResource(id = R.string.shopping_lists_screen_cart_icon),
            )

            NameTextField(
                modifier = Modifier
                    .weight(1f),
                listName = listName,
                onEvent = onEvent
            )

            Icon(
                modifier = Modifier
                    .height(Dimens.Large)
                    .clickable(
                        enabled = listName
                            .isBlank()
                            .not(),
                        onClick = {
                            onEvent(ShoppingListsEvent.NewListSaved(listName))
                        }
                    ),
                imageVector = Icons.Default.Done,
                contentDescription = stringResource(id = R.string.shopping_lists_screen_add_new_list_done_content_description),
                tint = if (listName.isBlank()) {
                    LocalContentColor.current.copy(alpha = LocalContentColor.current.alpha / 2)
                } else {
                    LocalContentColor.current
                },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NameTextField(
    listName: String,
    onEvent: (ShoppingListsEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }

    BasicTextField(
        modifier = modifier
            .padding(start = Dimens.Medium)
            .showKeyboard(),
        textStyle = LocalTextStyle.current,
        value = listName,
        onValueChange = {
            onEvent(ShoppingListsEvent.NewListNameChanged(it))
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = if (listName.isBlank()) ImeAction.None else ImeAction.Done,
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onEvent(ShoppingListsEvent.NewListSaved(listName))
            },
        ),
        interactionSource = interactionSource,
        decorationBox = @Composable { innerTextField ->
            TextFieldDefaults.DecorationBox(
                value = listName,
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
private fun NewShoppingListDialogContentPreview() {
    AppTheme {
        NewShoppingListDialogContent(
            listName = "Test",
            onEvent = {}
        )
    }
}
