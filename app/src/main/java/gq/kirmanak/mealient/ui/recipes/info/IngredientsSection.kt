package gq.kirmanak.mealient.ui.recipes.info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.database.recipe.entity.RecipeIngredientEntity
import gq.kirmanak.mealient.ui.Dimens

@Composable
internal fun IngredientsSection(
    ingredients: List<RecipeIngredientEntity>,
    isEditMode: Boolean,
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
                var ingredientsCount = 0
                ingredients.forEach { item ->
                    val isLastItem = (ingredientsCount == ingredients.size -1)
                    IngredientListItem(
                        item = item,
                        index = ingredientsCount++,
                        isEditMode = isEditMode,
                        isLastItem = isLastItem,
                    )
                }
            }
        }
    }
}

@Composable
private fun IngredientListItem(
    item: RecipeIngredientEntity,
    index: Int,
    modifier: Modifier = Modifier,
    isEditMode: Boolean,
    isLastItem: Boolean,
) {
    val title = item.title ?: ""

    if (isEditMode) {
        Row {
            OutlinedTextField(
                value = title,
                label = { Text(text = "title") },
                onValueChange = { /*ToDo*/ }
            )
            if (!isLastItem) {
                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.padding(4.dp).size(64.dp)
                ) {
                    Icon(Icons.Default.ArrowDownward, contentDescription = "Save")
                }
            }
            if (index > 0) {
                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.padding(4.dp).size(64.dp)
                ) {
                    Icon(Icons.Default.ArrowUpward, contentDescription = "Save")
                }
            }
        }
        if (item.disableAmount) {
            OutlinedTextField(
                value = item.display,
                label = { Text(text = "ingredient")},
                onValueChange = { /*ToDo*/ } )
        }
        else {
            OutlinedTextField(
                value = item.note,
                label = { Text(text = "ingredient")},
                onValueChange = { /*ToDo*/ } )
        }
    }
    else {

        var isChecked by rememberSaveable { mutableStateOf(false) }

        if (!title.isNullOrBlank()) {
            Text(
                modifier = modifier
                    .padding(horizontal = Dimens.Medium),
                text = title,
                style = MaterialTheme.typography.titleMedium,
            )

            Divider()
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