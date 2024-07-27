package gq.kirmanak.mealient.ui.recipes.info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.database.recipe.entity.RecipeIngredientEntity
import gq.kirmanak.mealient.ui.Dimens

@Composable
internal fun IngredientsSection(
    ingredients: List<RecipeIngredientEntity>,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Dimens.Small, Alignment.Top),
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = Dimens.Large),
            text = stringResource(id = R.string.fragment_recipe_info_ingredients_header),
            style = MaterialTheme.typography.titleLarge,
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.Small),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.Small),
                verticalArrangement = Arrangement.spacedBy(Dimens.Small, Alignment.Top),
            ) {
                ingredients.forEach { item ->
                    IngredientListItem(
                        item = item,
                    )
                }
            }
        }
    }
}

@Composable
private fun IngredientListItem(
    item: RecipeIngredientEntity,
    modifier: Modifier = Modifier,
) {
    var isChecked by rememberSaveable { mutableStateOf(false) }

    val title = item.title
    if (!title.isNullOrBlank()) {
        Text(
            modifier = modifier
                .padding(horizontal = Dimens.Medium),
            text = title,
            style = MaterialTheme.typography.titleMedium,
        )

        HorizontalDivider(
            color = LocalContentColor.current
        )
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Dimens.Small, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = { isChecked = it },
        )

        val (text, note) = item.textAndNote
        Column {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
            )

            if (note.isNotBlank()) {
                Text(
                    text = item.note,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

private val RecipeIngredientEntity.textAndNote: Pair<String, String>
    get() = when {
        disableAmount -> note to ""

        note.isBlank() -> display to ""

        else -> {
            val text = if (display.endsWith(note)) {
                display.dropLast(note.length).trimEnd()
            } else {
                display
            }

            text to note
        }
    }