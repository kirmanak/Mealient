package gq.kirmanak.mealient.ui.recipes.info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import gq.kirmanak.mealient.ui.AppTheme
import gq.kirmanak.mealient.ui.Dimens
import gq.kirmanak.mealient.ui.preview.ColorSchemePreview

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RecipeScreen(
    uiState: RecipeInfoUiState,
) {
    KeepScreenOn()

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .verticalScroll(
                    state = rememberScrollState(),
                )
                .padding(padding)
                .consumeWindowInsets(padding),
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
}

@ColorSchemePreview
@Composable
private fun RecipeScreenPreview() {
    AppTheme {
        RecipeScreen(
            uiState = RecipeInfoUiState(
                showIngredients = true,
                showInstructions = true,
                summaryEntity = SUMMARY_ENTITY,
                recipeIngredients = INGREDIENTS,
                recipeInstructions = INSTRUCTIONS,
                title = "Recipe title",
                description = "Recipe description",
            )
        )
    }
}

