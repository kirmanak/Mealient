package gq.kirmanak.mealient.shopping_lists.ui.composables

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ErrorSnackbar(
    error: Throwable,
    snackbarHostState: SnackbarHostState,
    onSnackbarShown: () -> Unit,
) {
    val text = getErrorMessage(error = error)
    val scope = rememberCoroutineScope()

    LaunchedEffect(snackbarHostState) {
        scope.launch {
            snackbarHostState.showSnackbar(message = text)
            onSnackbarShown()
        }
    }
}