package gq.kirmanak.mealient.ui.recipes.info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.database.recipe.entity.RecipeIngredientEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeInstructionEntity
import gq.kirmanak.mealient.ui.Dimens

@Composable
internal fun InstructionsSection(
    instructions: Map<RecipeInstructionEntity, List<RecipeIngredientEntity>>,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Dimens.Small, Alignment.Top),
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = Dimens.Large),
            text = stringResource(id = R.string.fragment_recipe_info_instructions_header),
            style = MaterialTheme.typography.titleLarge,
        )

        var stepCount = 0
        instructions.forEach { (instruction, ingredients) ->
            InstructionListItem(
                modifier = Modifier
                    .padding(horizontal = Dimens.Small),
                item = instruction,
                ingredients = ingredients,
                index = stepCount++,
            )
        }
    }
}

@Composable
private fun InstructionListItem(
    item: RecipeInstructionEntity,
    index: Int,
    ingredients: List<RecipeIngredientEntity>,
    modifier: Modifier = Modifier,
) {
    val title = item.title

    if (!title.isNullOrBlank()) {
        Text(
            modifier = modifier
                .padding(horizontal = Dimens.Medium),
            text = title,
            style = MaterialTheme.typography.titleMedium,
        )

    }

    Card(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .padding(Dimens.Medium),
            verticalArrangement = Arrangement.spacedBy(Dimens.Small, Alignment.Top),
        ) {
            Text(
                text = stringResource(
                    R.string.view_holder_recipe_instructions_step,
                    index + 1
                ),
                style = MaterialTheme.typography.titleSmall,
            )

            Text(
                text = item.text.trim(),
                style = MaterialTheme.typography.bodyMedium,
            )

            if (ingredients.isNotEmpty()) {
                Divider()
                ingredients.forEach { ingredient ->
                    Text(
                        text = ingredient.display,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}