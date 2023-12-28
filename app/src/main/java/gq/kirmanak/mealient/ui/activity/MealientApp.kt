package gq.kirmanak.mealient.ui.activity

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.navigation.popUpTo
import com.ramcosta.composedestinations.rememberNavHostEngine
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavHostEngine
import com.ramcosta.composedestinations.spec.Route
import com.ramcosta.composedestinations.utils.contains
import com.ramcosta.composedestinations.utils.currentDestinationAsState
import com.ramcosta.composedestinations.utils.startDestination
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.ui.AppTheme
import gq.kirmanak.mealient.ui.NavGraphs
import kotlinx.coroutines.launch

@Composable
internal fun MealientApp(
    viewModel: MainActivityViewModel,
) {
    val state by viewModel.appState.collectAsState()

    MealientApp(
        state = state,
        onEvent = viewModel::onEvent,
    )
}

@Composable
private fun MealientApp(
    state: MealientAppState,
    onEvent: (AppEvent) -> Unit,
) {
    val engine = rememberNavHostEngine()
    val controller = engine.rememberNavController()

    val currentDestinationState = controller.currentDestinationAsState()
    val currentDestination = currentDestinationState.value

    ForceNavigationEffect(
        currentDestination = currentDestination,
        appState = state,
        controller = controller
    )

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    val enableNavigationDrawer = enableNavigationDrawer(
        currentDestination = currentDestination,
        forcedDestination = state.forcedRoute,
    )

    AppTheme {
        ModalNavigationDrawer(
            drawerContent = {
                DrawerContent(
                    currentDestination = currentDestination,
                    onNavigation = {
                        coroutineScope.launch {
                            drawerState.close()
                            controller.navigate(it) { launchSingleTop = true }
                        }
                    },
                    onEvent = {
                        coroutineScope.launch {
                            drawerState.close()
                            onEvent(it)
                        }
                    }
                )
            },
            drawerState = drawerState,
            gesturesEnabled = enableNavigationDrawer,
        ) {
            AppContent(
                engine = engine,
                controller = controller,
                startRoute = (state.forcedRoute as? ForcedDestination.Destination)?.route,
                displayNavigationDrawer = enableNavigationDrawer,
                onOpenDrawerButtonClick = {
                    coroutineScope.launch { drawerState.open() }
                },
            )
        }
    }
}

@Composable
private fun ForceNavigationEffect(
    currentDestination: DestinationSpec<*>?,
    appState: MealientAppState,
    controller: NavHostController
) {
    LaunchedEffect(currentDestination, appState.forcedRoute) {
        if (
            appState.forcedRoute is ForcedDestination.Destination &&
            currentDestination != null &&
            currentDestination != appState.forcedRoute.route
        ) {
            controller.navigate(appState.forcedRoute.route) {
                popUpTo(currentDestination) {
                    inclusive = true
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun AppContent(
    engine: NavHostEngine,
    controller: NavHostController,
    startRoute: Route?,
    displayNavigationDrawer: Boolean,
    onOpenDrawerButtonClick: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                displayNavigationDrawer = displayNavigationDrawer,
                onOpenDrawerButtonClick = onOpenDrawerButtonClick,
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { paddingValues ->
        DestinationsNavHost(
            modifier = Modifier
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues),
            navGraph = NavGraphs.root,
            engine = engine,
            navController = controller,
            startRoute = startRoute ?: NavGraphs.root.startRoute,
            dependenciesContainerBuilder = {
                dependency(snackbarHostState)
            }
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TopAppBar(
    displayNavigationDrawer: Boolean,
    onOpenDrawerButtonClick: () -> Unit
) {
    TopAppBar(
        title = { },
        navigationIcon = {
            if (displayNavigationDrawer) {
                OpenDrawerIconButton(
                    onClick = onOpenDrawerButtonClick,
                )
            }
        }
    )
}

@Composable
private fun OpenDrawerIconButton(
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
    ) {
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = stringResource(R.string.view_toolbar_navigation_icon_content_description),
        )
    }
}

private fun enableNavigationDrawer(
    currentDestination: DestinationSpec<*>?,
    forcedDestination: ForcedDestination,
): Boolean = when {
    currentDestination == null || forcedDestination != ForcedDestination.None -> false

    NavGraphs.root.nestedNavGraphs.any {
        it.contains(currentDestination) && it.startDestination != currentDestination
    } -> false

    else -> true
}