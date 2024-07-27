package gq.kirmanak.mealient.shopping_lists.ui.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import gq.kirmanak.mealient.ui.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EditableItemBox(
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    content: @Composable (RowScope.() -> Unit),
    deleteContentDescription: String,
    editContentDescription: String,
    modifier: Modifier = Modifier
) {
    val dismissState = rememberSwipeToDismissBoxState()

    LaunchedEffect(dismissState.currentValue, onDelete, onEdit) {
        when (dismissState.currentValue) {
            SwipeToDismissBoxValue.EndToStart -> onDelete()
            SwipeToDismissBoxValue.StartToEnd -> onEdit()
            SwipeToDismissBoxValue.Settled -> Unit
        }
    }

    SwipeToDismissBox(
        modifier = modifier,
        state = dismissState,
        content = content,
        backgroundContent = {
            if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart) {
                EditableItemBoxBackground(
                    icon = Icons.Default.Delete,
                    backgroundColor = MaterialTheme.colorScheme.error,
                    iconAlignment = Alignment.CenterEnd,
                    contentDescription = deleteContentDescription
                )
            } else if (dismissState.targetValue == SwipeToDismissBoxValue.StartToEnd) {
                EditableItemBoxBackground(
                    icon = Icons.Default.Edit,
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    iconAlignment = Alignment.CenterStart,
                    contentDescription = editContentDescription
                )
            }
        },
    )
}

@Composable
private fun EditableItemBoxBackground(
    icon: ImageVector,
    backgroundColor: Color,
    iconAlignment: Alignment,
    contentDescription: String,
    modifier: Modifier = Modifier,
    contentColor: Color = MaterialTheme.colorScheme.contentColorFor(backgroundColor)
) {
    val color by animateColorAsState(backgroundColor, label = "background-color")
    val iconColor by animateColorAsState(contentColor, label = "icon-color")
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color)
    ) {
        Icon(
            modifier = Modifier
                .align(iconAlignment)
                .padding(horizontal = Dimens.Small),
            imageVector = icon,
            contentDescription = contentDescription,
            tint = iconColor
        )
    }
}