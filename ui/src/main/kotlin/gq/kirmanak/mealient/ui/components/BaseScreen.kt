package gq.kirmanak.mealient.ui.components

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import gq.kirmanak.mealient.ui.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BaseScreen(
    topAppBar: @Composable () -> Unit = { },
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    content: @Composable (Modifier) -> Unit,
) {
    Scaffold(
        topBar = { topAppBar() },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { paddingValues ->
        content(
            Modifier
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues),
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BaseScreenWithNavigation(
    baseScreenState: BaseScreenState,
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    topAppBar: @Composable () -> Unit = { NavigationTopAppBar(drawerState) },
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    content: @Composable (Modifier) -> Unit,
) {
    ModalNavigationDrawer(
        drawerContent = {
            DrawerContent(
                drawerState = drawerState,
                drawerItems = baseScreenState.drawerItems,
            )
        },
        drawerState = drawerState,
    ) {
        Scaffold(
            topBar = { topAppBar() },
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { paddingValues ->
            content(
                Modifier
                    .padding(paddingValues)
                    .consumeWindowInsets(paddingValues)
            )
        }
    }
}

class BaseScreenState internal constructor(
    drawerItems: List<DrawerItem>,
) {

    val drawerItems by mutableStateOf(drawerItems)

}

@Composable
fun rememberBaseScreenState(
    drawerItems: List<DrawerItem>,
): BaseScreenState {
    return remember {
        BaseScreenState(
            drawerItems = drawerItems,
        )
    }
}

@Composable
fun previewBaseScreenState(): BaseScreenState {
    return rememberBaseScreenState(
        drawerItems = emptyList(),
    )

}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun NavigationTopAppBar(
    drawerState: DrawerState,
) {
    TopAppBar(
        title = { },
        navigationIcon = {
            OpenDrawerIconButton(
                drawerState = drawerState,
            )
        },
    )
}

@Composable
fun OpenDrawerIconButton(
    drawerState: DrawerState,
) {
    val coroutineScope = rememberCoroutineScope()

    IconButton(
        modifier = Modifier
            .semantics { testTag = "open-drawer-button" },
        onClick = {
            coroutineScope.launch {
                drawerState.open()
            }
        },
    ) {
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = stringResource(R.string.view_toolbar_navigation_icon_content_description),
        )
    }
}