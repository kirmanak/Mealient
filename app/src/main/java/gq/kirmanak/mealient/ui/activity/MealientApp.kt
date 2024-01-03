package gq.kirmanak.mealient.ui.activity

import android.content.Intent
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
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
import com.ramcosta.composedestinations.utils.currentDestinationAsState
import gq.kirmanak.mealient.ui.NavGraphs
import gq.kirmanak.mealient.ui.components.rememberBaseScreenState

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
        controller = controller,
        forcedDestination = state.forcedRoute
    )

    IntentLaunchEffect(
        intent = state.intentToLaunch,
        onEvent = onEvent,
    )

    if (state.dialogState != null) {
        MealientDialog(
            dialogState = state.dialogState,
            onEvent = onEvent,
        )
    }

    AppContent(
        engine = engine,
        controller = controller,
        startRoute = (state.forcedRoute as? ForcedDestination.Destination)?.route,
        onEvent = onEvent,
    )
}

@Composable
private fun IntentLaunchEffect(
    intent: Intent?,
    onEvent: (AppEvent) -> Unit,
) {
    val context = LocalContext.current
    LaunchedEffect(intent) {
        if (intent != null) {
            context.startActivity(intent)
            onEvent(AppEvent.LaunchedIntent)
        }
    }
}

@Composable
private fun MealientDialog(
    dialogState: DialogState,
    onEvent: (AppEvent) -> Unit,
) {
    AlertDialog(
        onDismissRequest = {
            onEvent(dialogState.onDismiss)
        },
        confirmButton = {
            TextButton(
                onClick = { onEvent(dialogState.onPositiveClick) },
            ) {
                Text(
                    text = stringResource(id = dialogState.positiveButton),
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onEvent(dialogState.onNegativeClick) },
            ) {
                Text(
                    text = stringResource(id = dialogState.negativeButton),
                )
            }
        },
        title = {
            Text(
                text = stringResource(id = dialogState.title),
            )
        },
        text = {
            Text(
                text = stringResource(id = dialogState.message),
            )
        },
    )
}

@Composable
private fun ForceNavigationEffect(
    currentDestination: DestinationSpec<*>?,
    controller: NavHostController,
    forcedDestination: ForcedDestination,
) {
    LaunchedEffect(currentDestination, forcedDestination) {
        if (
            forcedDestination is ForcedDestination.Destination &&
            currentDestination != null &&
            currentDestination != forcedDestination.route
        ) {
            controller.navigate(forcedDestination.route) {
                popUpTo(currentDestination) {
                    inclusive = true
                }
            }
        }
    }
}

@Composable
private fun AppContent(
    engine: NavHostEngine,
    controller: NavHostController,
    startRoute: Route?,
    onEvent: (AppEvent) -> Unit,
) {
    val drawerItems = createDrawerItems(
        navController = controller,
        onEvent = onEvent,
    )
    val baseScreenState = rememberBaseScreenState(
        drawerItems = drawerItems,
    )

    DestinationsNavHost(
        navGraph = NavGraphs.root,
        engine = engine,
        navController = controller,
        startRoute = startRoute ?: NavGraphs.root.startRoute,
        dependenciesContainerBuilder = {
            dependency(baseScreenState)
        }
    )
}

