package gq.kirmanak.mealient.ui.recipes.info

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import gq.kirmanak.mealient.ui.AppTheme
import gq.kirmanak.mealient.ui.Dimens
import gq.kirmanak.mealient.ui.components.BaseScreen
import gq.kirmanak.mealient.ui.preview.ColorSchemePreview

data class RecipeScreenArgs(
    val recipeId: String,
)

@Destination(
    navArgsDelegate = RecipeScreenArgs::class,
)
@Composable
internal fun RecipeScreen(
    viewModel: RecipeInfoViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()

    var isEditMode by remember { mutableStateOf(false ) }

    BaseScreen { modifier ->
        RecipeScreen(
            modifier = modifier,
            state = state,
            changeEditMode = { isEditMode = !isEditMode },
            isEditMode = isEditMode
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun RecipeScreen(
    state: RecipeInfoUiState,
    modifier: Modifier = Modifier,
    changeEditMode: () -> Unit,
    isEditMode: Boolean,
) {
    KeepScreenOn()

    Scaffold(
        floatingActionButton = {
            Row {
                FloatingActionButton(
                    onClick = {
                        changeEditMode()
                    },
                    modifier = Modifier.padding( horizontal = 8.dp)
                ) {
                    if (isEditMode) {
                        Icon(Icons.Default.Cancel, contentDescription = "Cancel")
                    } else {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                }
                if (isEditMode) {
                    FloatingActionButton(
                        onClick = {
                            changeEditMode()
                            save()
                        },
                        modifier = Modifier.padding( horizontal = 8.dp)
                    ) {
                        Icon(Icons.Default.Save, contentDescription = "Save")
                    }
                }
            }
        }
    )
    {
        Column(
            modifier = modifier
                .verticalScroll(
                    state = rememberScrollState(),
                ),
            verticalArrangement = Arrangement.spacedBy(Dimens.Small, Alignment.Top),
        ) {
            HeaderSection(
                imageUrl = state.imageUrl,
                title = state.title ?: "",
                description = state.description ?: "",
                isEditMode = isEditMode,
            )

            if (state.showIngredients) {
                IngredientsSection(
                    ingredients = state.recipeIngredients,
                    isEditMode = isEditMode,
                )
            }

            if (state.showInstructions) {
                InstructionsSection(
                    instructions = state.recipeInstructions,
                    isEditMode = isEditMode,
                )
            }
        }
    }
}

private fun save( ) {

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
                locked = false,
                editable = true,
            ),
            changeEditMode = {},
            isEditMode = false,
        )
    }
}

