package gq.kirmanak.mealient.ui.components

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ErrorSnackbar(
    text: String?,
    snackbarHostState: SnackbarHostState,
    onSnackbarShown: () -> Unit,
) {
    if (text.isNullOrBlank()) {
        snackbarHostState.currentSnackbarData?.dismiss()
        return
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(snackbarHostState) {
        scope.launch {
            snackbarHostState.showSnackbar(message = text)
            onSnackbarShown()
        }
    }
}