package gq.kirmanak.mealient.ui.activity

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.Direction
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.ramcosta.composedestinations.utils.contains
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.shopping_lists.ui.destinations.ShoppingListsScreenDestination
import gq.kirmanak.mealient.ui.AppTheme
import gq.kirmanak.mealient.ui.Dimens
import gq.kirmanak.mealient.ui.NavGraphs
import gq.kirmanak.mealient.ui.destinations.AddRecipeScreenDestination
import gq.kirmanak.mealient.ui.destinations.BaseURLScreenDestination
import gq.kirmanak.mealient.ui.destinations.RecipesListDestination
import gq.kirmanak.mealient.ui.preview.ColorSchemePreview

@Composable
internal fun DrawerContent(
    currentDestination: DestinationSpec<*>?,
    onNavigation: (Direction) -> Unit,
    onEvent: (AppEvent) -> Unit,
) {
    ModalDrawerSheet {
        Text(
            modifier = Modifier
                .padding(Dimens.Medium),
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
        )

        NavigationDrawerItem(
            menuName = R.string.menu_navigation_drawer_recipes_list,
            currentDestination = currentDestination,
            destination = RecipesListDestination,
            icon = Icons.Default.List,
            onNavigation = onNavigation,
        )

        NavigationDrawerItem(
            menuName = R.string.menu_navigation_drawer_add_recipe,
            currentDestination = currentDestination,
            destination = AddRecipeScreenDestination,
            icon = Icons.Default.Add,
            onNavigation = onNavigation,
        )

        NavigationDrawerItem(
            menuName = R.string.menu_navigation_drawer_shopping_lists,
            currentDestination = currentDestination,
            destination = NavGraphs.shoppingLists,
            icon = Icons.Default.ShoppingCart,
            onNavigation = onNavigation,
        )

        NavigationDrawerItem(
            menuName = R.string.menu_navigation_drawer_change_url,
            currentDestination = currentDestination,
            destination = BaseURLScreenDestination,
            icon = Icons.Default.SyncAlt,
            onNavigation = onNavigation,
        )

        NavigationDrawerItem(
            menuName = R.string.menu_navigation_drawer_logout,
            selected = false,
            icon = Icons.Default.Logout,
            onClick = { onEvent(AppEvent.Logout) },
        )

        NavigationDrawerItem(
            menuName = R.string.menu_navigation_drawer_email_logs,
            selected = false,
            icon = Icons.Default.Email,
            onClick = { onEvent(AppEvent.EmailLogs) },
        )
    }
}

@Composable
private fun NavigationDrawerItem(
    menuName: Int,
    currentDestination: DestinationSpec<*>?,
    destination: Direction,
    icon: ImageVector,
    onNavigation: (Direction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val selected = isDestinationSelected(currentDestination, destination)
    val onClick = { onNavigation(destination) }
    NavigationDrawerItem(
        modifier = modifier,
        menuName = menuName,
        selected = selected,
        icon = icon,
        onClick = onClick
    )
}

@Composable
private fun NavigationDrawerItem(
    menuName: Int,
    selected: Boolean,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationDrawerItem(
        modifier = modifier
            .padding(horizontal = Dimens.Medium),
        label = {
            Text(
                text = stringResource(id = menuName),
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

private fun isDestinationSelected(
    currentDestination: DestinationSpec<*>?,
    direction: Direction,
): Boolean = when {
    currentDestination == null -> false
    currentDestination == direction -> true
    direction is NavGraphSpec && direction.contains(currentDestination) -> true
    else -> false
}

@ColorSchemePreview
@Composable
private fun DrawerContentPreview() {
    AppTheme {
        DrawerContent(
            currentDestination = ShoppingListsScreenDestination,
            onNavigation = { },
            onEvent = { },
        )
    }
}
