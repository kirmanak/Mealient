package gq.kirmanak.mealient.ui.recipes.list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.datasource.NetworkError
import gq.kirmanak.mealient.ui.AppTheme
import gq.kirmanak.mealient.ui.Dimens
import gq.kirmanak.mealient.ui.components.EmptyListError
import gq.kirmanak.mealient.ui.preview.ColorSchemePreview
import kotlinx.coroutines.flow.emptyFlow

@Composable
internal fun RecipesListError(
    recipes: LazyPagingItems<RecipeListItemState>,
    modifier: Modifier
) {
    val error = when (val state = recipes.loadState.refresh) {
        is LoadState.Error -> getErrorMessage(state)
        is LoadState.Loading,
        is LoadState.NotLoading -> stringResource(id = R.string.fragment_recipes_list_no_recipes)
    }
    EmptyListError(
        modifier = modifier
            .fillMaxSize()
            .padding(Dimens.Large),
        text = error,
    )
}

@Composable
private fun getErrorMessage(state: LoadState.Error): String {
    val reason = when (state.error) {
        is NetworkError.Unauthorized -> stringResource(R.string.fragment_recipes_load_failure_toast_unauthorized)
        is NetworkError.NoServerConnection -> stringResource(R.string.fragment_recipes_load_failure_toast_no_connection)
        is NetworkError.NotMealie, is NetworkError.MalformedUrl -> stringResource(id = R.string.fragment_recipes_load_failure_toast_unexpected_response)
        else -> null
    }
    return if (reason == null) {
        stringResource(R.string.fragment_recipes_load_failure_toast_no_reason)
    } else {
        stringResource(R.string.fragment_recipes_load_failure_toast, reason)
    }
}

@ColorSchemePreview
@Composable
private fun RecipesListErrorPreview() {
    AppTheme {
        RecipesListError(
            recipes = emptyFlow<PagingData<RecipeListItemState>>().collectAsLazyPagingItems(),
            modifier = Modifier.fillMaxSize()
        )
    }
}