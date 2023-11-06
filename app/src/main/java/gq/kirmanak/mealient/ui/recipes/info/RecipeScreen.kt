package gq.kirmanak.mealient.ui.recipes.info

import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.database.recipe.entity.RecipeIngredientEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeInstructionEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.ui.AppTheme
import gq.kirmanak.mealient.ui.Dimens
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

@Composable
fun RecipeScreen(
    uiState: RecipeInfoUiState,
) {
    Column(
        modifier = Modifier
            .verticalScroll(
                state = rememberScrollState(),
            ),
        verticalArrangement = Arrangement.spacedBy(Dimens.Small, Alignment.Top),
    ) {
        HeaderSection(
            imageUrl = uiState.imageUrl,
            title = uiState.title,
            description = uiState.description,
        )

        if (uiState.showIngredients) {
            IngredientsSection(
                ingredients = uiState.recipeIngredients,
            )
        }

        if (uiState.showInstructions) {
            InstructionsSection(
                instructions = uiState.recipeInstructions,
            )
        }
    }
}

@Composable
private fun HeaderSection(
    imageUrl: String?,
    title: String?,
    description: String?,
) {
    val imageFallback = painterResource(id = R.drawable.placeholder_recipe)

    AsyncImage(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(2f) // 2:1
            .clip(
                RoundedCornerShape(
                    topEnd = 0.dp,
                    topStart = 0.dp,
                    bottomEnd = Dimens.Intermediate,
                    bottomStart = Dimens.Intermediate,
                )
            ),
        model = imageUrl,
        contentDescription = stringResource(id = R.string.content_description_fragment_recipe_info_image),
        placeholder = imageFallback,
        error = imageFallback,
        fallback = imageFallback,
        contentScale = ContentScale.Crop,
    )

    if (!title.isNullOrEmpty()) {
        Text(
            modifier = Modifier
                .padding(horizontal = Dimens.Small),
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }

    if (!description.isNullOrEmpty()) {
        Text(
            modifier = Modifier
                .padding(horizontal = Dimens.Small),
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun InstructionsSection(
    instructions: Map<RecipeInstructionEntity, List<RecipeIngredientEntity>>,
) {
    Text(
        modifier = Modifier
            .padding(horizontal = Dimens.Large),
        text = stringResource(id = R.string.fragment_recipe_info_instructions_header),
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onSurface,
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

@Composable
private fun IngredientsSection(
    ingredients: List<RecipeIngredientEntity>,
) {
    Text(
        modifier = Modifier
            .padding(horizontal = Dimens.Large),
        text = stringResource(id = R.string.fragment_recipe_info_ingredients_header),
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onSurface,
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
            color = MaterialTheme.colorScheme.onSurface,
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
                color = MaterialTheme.colorScheme.onSurface,
            )

            Text(
                text = item.text.trim(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )

            if (ingredients.isNotEmpty()) {
                Divider(
                    color = MaterialTheme.colorScheme.outline,
                )
                ingredients.forEach { ingredient ->
                    Text(
                        text = ingredient.display,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,
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
            color = MaterialTheme.colorScheme.onSurface,
        )

        Divider(
            color = MaterialTheme.colorScheme.outline,
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

        Text(
            text = item.display,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RecipeScreenPreview() {
    AppTheme {
        RecipeScreen(
            uiState = previewUiState()
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_MASK and UI_MODE_NIGHT_YES)
@Composable
private fun RecipeScreenNightPreview() {
    AppTheme {
        RecipeScreen(
            uiState = previewUiState()
        )
    }
}

private fun previewUiState(): RecipeInfoUiState {
    val ingredient = RecipeIngredientEntity(
        id = "2",
        recipeId = "1",
        note = "Recipe ingredient note",
        food = "Recipe ingredient food",
        unit = "Recipe ingredient unit",
        display = "Recipe ingredient display that is very long and should be wrapped",
        quantity = 1.0,
        title = null,
    )
    val uiState = RecipeInfoUiState(
        showIngredients = true,
        showInstructions = true,
        summaryEntity = RecipeSummaryEntity(
            remoteId = "1",
            name = "Recipe name",
            slug = "recipe-name",
            description = "Recipe description",
            dateAdded = LocalDate(2021, 1, 1),
            dateUpdated = LocalDateTime(2021, 1, 1, 1, 1, 1),
            imageId = null,
            isFavorite = false,
        ),
        recipeIngredients = listOf(
            RecipeIngredientEntity(
                id = "1",
                recipeId = "1",
                note = "Recipe ingredient note",
                food = "Recipe ingredient food",
                unit = "Recipe ingredient unit",
                display = "Recipe ingredient display that is very long and should be wrapped",
                quantity = 1.0,
                title = "Recipe ingredient section title",
            ),
            ingredient,
        ),
        recipeInstructions = mapOf(
            RecipeInstructionEntity(
                id = "1",
                recipeId = "1",
                text = "Recipe instruction",
                title = "Section title",
            ) to emptyList(),
            RecipeInstructionEntity(
                id = "2",
                recipeId = "1",
                text = "Recipe instruction",
                title = "",
            ) to listOf(ingredient),
        ),
        title = "Recipe title",
        description = "Recipe description",
    )
    return uiState
}

