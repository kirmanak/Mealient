package gq.kirmanak.mealient.ui.recipes.info

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
                disableAmounts = uiState.disableAmounts,
            )
        }

        if (uiState.showInstructions) {
            InstructionsSection(uiState.recipeInstructions)
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
            .clip(RoundedCornerShape(Dimens.Intermediate)),
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
        )
    }

    if (!description.isNullOrEmpty()) {
        Text(
            modifier = Modifier
                .padding(horizontal = Dimens.Small),
            text = description,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
private fun InstructionsSection(
    instructions: List<RecipeInstructionEntity>,
) {
    Text(
        modifier = Modifier
            .padding(horizontal = Dimens.Large),
        text = stringResource(id = R.string.fragment_recipe_info_instructions_header),
        style = MaterialTheme.typography.titleLarge,
    )

    instructions.forEachIndexed { index, item ->
        InstructionListItem(
            modifier = Modifier
                .padding(horizontal = Dimens.Small),
            item = item,
            index = index,
        )
    }
}

@Composable
private fun IngredientsSection(
    ingredients: List<RecipeIngredientEntity>,
    disableAmounts: Boolean,
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
                .padding(horizontal = Dimens.Small),
            verticalArrangement = Arrangement.spacedBy(Dimens.Small, Alignment.Top),
        ) {
            ingredients.forEach { item ->
                IngredientListItem(
                    item = item,
                    disableAmounts = disableAmounts,
                )
            }
        }
    }
}

@Composable
private fun InstructionListItem(
    item: RecipeInstructionEntity,
    index: Int,
    modifier: Modifier = Modifier,
) {
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
                style = MaterialTheme.typography.titleLarge,
            )

            Text(
                text = item.text.trim(),
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Composable
private fun IngredientListItem(
    item: RecipeIngredientEntity,
    disableAmounts: Boolean,
    modifier: Modifier = Modifier,
) {
    var isChecked by rememberSaveable { mutableStateOf(false) }

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
            text = item.getText(disableAmounts),
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

private fun RecipeIngredientEntity.getText(
    disableAmounts: Boolean,
): String {
    return if (disableAmounts) {
        note.trim()
    } else {
        val builder = StringBuilder()
        quantity?.let { builder.append("${it.formatUsingMediantMethod()} ") }
        unit?.let { builder.append("$it ") }
        food?.let { builder.append("$it ") }
        builder.append(note)
        builder.toString().trim()
    }
}

@Preview(showBackground = true)
@Composable
private fun RecipeScreenPreview() {
    AppTheme {
        RecipeScreen(
            uiState = RecipeInfoUiState(
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
                        localId = 1,
                        recipeId = "1",
                        note = "Recipe ingredient note",
                        food = "Recipe ingredient food",
                        unit = "Recipe ingredient unit",
                        quantity = 1.0,
                        title = "Recipe ingredient title",
                    ),
                    RecipeIngredientEntity(
                        localId = 2,
                        recipeId = "1",
                        note = "Recipe ingredient note",
                        food = "Recipe ingredient food",
                        unit = "Recipe ingredient unit",
                        quantity = 1.0,
                        title = "Recipe ingredient title",
                    ),
                ),
                recipeInstructions = listOf(
                    RecipeInstructionEntity(
                        localId = 1,
                        recipeId = "1",
                        text = "Recipe instruction",
                    ),
                    RecipeInstructionEntity(
                        localId = 2,
                        recipeId = "1",
                        text = "Recipe instruction",
                    ),
                ),
                title = "Recipe title",
                description = "Recipe description",
                disableAmounts = false,
            )
        )

    }
}

