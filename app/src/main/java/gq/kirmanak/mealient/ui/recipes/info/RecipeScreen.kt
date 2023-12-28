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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import gq.kirmanak.mealient.ui.AppTheme
import gq.kirmanak.mealient.ui.Dimens
import gq.kirmanak.mealient.ui.preview.ColorSchemePreview

data class RecipeScreenArgs(
    val recipeId: String,
)

@Destination(
    navArgsDelegate = RecipeScreenArgs::class,
)
@Composable
internal fun RecipeScreen(
    viewModel: RecipeInfoViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    RecipeScreen(
        state = state,
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RecipeScreen(
    state: RecipeInfoUiState,
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
                imageUrl = state.imageUrl,
                title = state.title,
                description = state.description,
            )

            if (state.showIngredients) {
                IngredientsSection(
                    ingredients = state.recipeIngredients,
                )
            }

            if (state.showInstructions) {
                InstructionsSection(
                    instructions = state.recipeInstructions,
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
            state = RecipeInfoUiState(
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

