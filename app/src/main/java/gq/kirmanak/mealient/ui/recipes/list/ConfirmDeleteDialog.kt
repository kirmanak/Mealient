package gq.kirmanak.mealient.ui.recipes.list

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import gq.kirmanak.mealient.R

@Composable
internal fun ConfirmDeleteDialog(
    onDismissRequest: () -> Unit,
    onConfirm: (RecipeListItemState) -> Unit,
    item: RecipeListItemState,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(item)
                },
            ) {
                Text(
                    text = stringResource(id = R.string.fragment_recipes_delete_recipe_confirm_dialog_positive_btn),
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest,
            ) {
                Text(
                    text = stringResource(id = R.string.fragment_recipes_delete_recipe_confirm_dialog_negative_btn),
                )
            }
        },
        title = {
            Text(
                text = stringResource(id = R.string.fragment_recipes_delete_recipe_confirm_dialog_title),
            )
        },
        text = {
            Text(
                text = stringResource(
                    id = R.string.fragment_recipes_delete_recipe_confirm_dialog_message,
                    item.entity.name
                ),
            )
        },
    )
}