package gq.kirmanak.mealient.ui.recipes.info

import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import gq.kirmanak.mealient.ui.AppTheme
import gq.kirmanak.mealient.ui.Dimens

@Composable
fun RecipeScreen(
    uiState: RecipeInfoUiState,
) {
    KeepScreenOn()

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

@Preview(showBackground = true)
@Composable
private fun RecipeScreenPreview() {
    AppTheme {
        RecipeScreen(
            uiState = RECIPE_INFO_UI_STATE
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_MASK and UI_MODE_NIGHT_YES)
@Composable
private fun RecipeScreenNightPreview() {
    AppTheme {
        RecipeScreen(
            uiState = RECIPE_INFO_UI_STATE
        )
    }
}

private val RECIPE_INFO_UI_STATE = RecipeInfoUiState(
    showIngredients = true,
    showInstructions = true,
    summaryEntity = SUMMARY_ENTITY,
    recipeIngredients = INGREDIENTS,
    recipeInstructions = INSTRUCTIONS,
    title = "Recipe title",
    description = "Recipe description",
)
