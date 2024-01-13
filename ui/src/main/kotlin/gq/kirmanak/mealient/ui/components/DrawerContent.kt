package gq.kirmanak.mealient.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import gq.kirmanak.mealient.ui.Dimens
import gq.kirmanak.mealient.ui.R

interface DrawerItem {

    @Composable
    fun getName(): String

    val icon: ImageVector

    @Composable
    fun isSelected(): Boolean

    val onClick: (DrawerState) -> Unit

}

@Composable
internal fun DrawerContent(
    drawerState: DrawerState,
    drawerItems: List<DrawerItem>,
) {
    ModalDrawerSheet {
        Text(
            modifier = Modifier
                .padding(Dimens.Medium),
            text = stringResource(id = R.string.menu_navigation_drawer_header),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
        )

        drawerItems.forEach { item ->
            NavigationDrawerItem(
                name = item.getName(),
                selected = item.isSelected(),
                icon = item.icon,
                onClick = { item.onClick(drawerState) },
            )
        }
    }
}

@Composable
private fun NavigationDrawerItem(
    name: String,
    selected: Boolean,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    androidx.compose.material3.NavigationDrawerItem(
        modifier = modifier
            .padding(horizontal = Dimens.Medium),
        label = {
            Text(
                text = name,
            )
        },
        selected = selected,
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
            )
        },
        onClick = onClick,
    )
}