package gq.kirmanak.mealient.shopping_lists.ui.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import gq.kirmanak.mealient.datasource.NetworkError
import gq.kirmanak.mealient.shopping_list.R

@Composable
fun getErrorMessage(error: Throwable): String = when (error) {
    is NetworkError.Unauthorized -> stringResource(R.string.shopping_lists_screen_unauthorized_error)
    is NetworkError.NoServerConnection -> stringResource(R.string.shopping_lists_screen_no_connection)
    else -> error.message ?: stringResource(R.string.shopping_lists_screen_unknown_error)
}