package gq.kirmanak.mealient.ui.activity

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.navigation.popUpTo
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.Direction
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.ramcosta.composedestinations.utils.contains
import com.ramcosta.composedestinations.utils.currentDestinationAsState
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.ui.NavGraphs
import gq.kirmanak.mealient.ui.components.DrawerItem
import gq.kirmanak.mealient.ui.destinations.AddRecipeScreenDestination
import gq.kirmanak.mealient.ui.destinations.BaseURLScreenDestination
import gq.kirmanak.mealient.ui.destinations.RecipesListDestination
import kotlinx.coroutines.launch

@Composable
internal fun createDrawerItems(
    navController: NavController,
    onEvent: (AppEvent) -> Unit
): List<DrawerItem> {
    val coroutineScope = rememberCoroutineScope()

    fun createNavigationItem(
        @StringRes nameRes: Int,
        icon: ImageVector,
        direction: Direction,
    ): DrawerItem = DrawerItemImpl(
        nameRes = nameRes,
        icon = icon,
        isSelected = {
            val currentDestination by navController.currentDestinationAsState()
            isDestinationSelected(currentDestination, direction)
        },
        onClick = { drawerState ->
            coroutineScope.launch {
                drawerState.close()
                navController.navigate(direction) {
                    launchSingleTop = true
                    popUpTo(NavGraphs.root)
                }
            }
        },
    )

    fun createActionItem(
        @StringRes nameRes: Int,
        icon: ImageVector,
        appEvent: AppEvent,
    ): DrawerItem = DrawerItemImpl(
        nameRes = nameRes,
        icon = icon,
        isSelected = { false },
        onClick = { drawerState ->
            coroutineScope.launch {
                drawerState.close()
                onEvent(appEvent)
            }
        },
    )

    return listOf(
        createNavigationItem(
            nameRes = R.string.menu_navigation_drawer_recipes_list,
            icon = Icons.Default.List,
            direction = RecipesListDestination,
        ),
        createNavigationItem(
            nameRes = R.string.menu_navigation_drawer_add_recipe,
            icon = Icons.Default.Add,
            direction = AddRecipeScreenDestination,
        ),
        createNavigationItem(
            nameRes = R.string.menu_navigation_drawer_shopping_lists,
            icon = Icons.Default.ShoppingCart,
            direction = NavGraphs.shoppingLists,
        ),
        createNavigationItem(
            nameRes = R.string.menu_navigation_drawer_change_url,
            icon = Icons.Default.SyncAlt,
            direction = BaseURLScreenDestination,
        ),
        createActionItem(
            nameRes = R.string.menu_navigation_drawer_logout,
            icon = Icons.Default.Logout,
            appEvent = AppEvent.Logout,
        ),
        createActionItem(
            nameRes = R.string.menu_navigation_drawer_email_logs,
            icon = Icons.Default.Email,
            appEvent = AppEvent.EmailLogs,
        )
    )
}

internal class DrawerItemImpl(
    @StringRes private val nameRes: Int,
    private val isSelected: @Composable () -> Boolean,
    override val icon: ImageVector,
    override val onClick: (DrawerState) -> Unit,
) : DrawerItem {

    @Composable
    override fun getName(): String = stringResource(id = nameRes)

    @Composable
    override fun isSelected(): Boolean = isSelected.invoke()
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