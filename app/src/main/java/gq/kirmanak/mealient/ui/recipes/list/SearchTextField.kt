package gq.kirmanak.mealient.ui.recipes.list

import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.ui.AppTheme
import gq.kirmanak.mealient.ui.preview.ColorSchemePreview

@Composable
internal fun SearchTextField(
    searchQuery: String,
    onValueChanged: (String) -> Unit,
    @StringRes placeholder: Int,
    modifier: Modifier = Modifier,
) {
    TextField(
        modifier = modifier,
        value = searchQuery,
        onValueChange = onValueChanged,
        placeholder = {
            Text(
                text = stringResource(id = placeholder),
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
            )
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
        ),
        keyboardActions = KeyboardActions(
            onSearch = { defaultKeyboardAction(ImeAction.Done) }
        ),
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Unspecified,
            unfocusedIndicatorColor = Color.Unspecified,
            disabledIndicatorColor = Color.Unspecified,
        )
    )
}

@ColorSchemePreview
@Composable
private fun SearchTextFieldPreview() {
    AppTheme {
        SearchTextField(
            searchQuery = "",
            onValueChanged = {},
            placeholder = R.string.search_recipes_hint,
        )
    }
}