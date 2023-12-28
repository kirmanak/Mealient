package gq.kirmanak.mealient.ui.add

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.ui.AppTheme
import gq.kirmanak.mealient.ui.Dimens
import gq.kirmanak.mealient.ui.components.TopProgressIndicator
import gq.kirmanak.mealient.ui.preview.ColorSchemePreview

@Destination
@Composable
internal fun AddRecipeScreen(
    snackbarHostState: SnackbarHostState,
    viewModel: AddRecipeViewModel = hiltViewModel()
) {
    val screenState by viewModel.screenState.collectAsState()

    AddRecipeScreen(
        snackbarHostState = snackbarHostState,
        state = screenState,
        onEvent = viewModel::onEvent,
    )
}

@Composable
internal fun AddRecipeScreen(
    snackbarHostState: SnackbarHostState,
    state: AddRecipeScreenState,
    onEvent: (AddRecipeScreenEvent) -> Unit,
) {
    state.snackbarMessage?.let {
        val message = when (it) {
            is AddRecipeSnackbarMessage.Error -> stringResource(id = R.string.fragment_add_recipe_save_error)
            is AddRecipeSnackbarMessage.Success -> stringResource(id = R.string.fragment_add_recipe_save_success)
        }
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message)
            onEvent(AddRecipeScreenEvent.SnackbarShown)
        }
    } ?: run {
        snackbarHostState.currentSnackbarData?.dismiss()
    }
    TopProgressIndicator(
        isLoading = state.isLoading,
    ) {
        AddRecipeScreenContent(
            state = state,
            onEvent = onEvent,
        )
    }
}

@Composable
private fun AddRecipeScreenContent(
    state: AddRecipeScreenState,
    onEvent: (AddRecipeScreenEvent) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(Dimens.Medium),
        verticalArrangement = Arrangement.spacedBy(Dimens.Medium),
        horizontalAlignment = Alignment.End,
    ) {
        item {
            AddRecipeInputField(
                input = state.recipeNameInput,
                label = stringResource(id = R.string.fragment_add_recipe_recipe_name),
                isLast = false,
                onValueChange = { onEvent(AddRecipeScreenEvent.RecipeNameInput(it)) },
            )
        }

        item {
            AddRecipeInputField(
                input = state.recipeDescriptionInput,
                label = stringResource(id = R.string.fragment_add_recipe_recipe_description),
                isLast = false,
                onValueChange = { onEvent(AddRecipeScreenEvent.RecipeDescriptionInput(it)) },
            )
        }

        item {
            AddRecipeInputField(
                input = state.recipeYieldInput,
                label = stringResource(id = R.string.fragment_add_recipe_recipe_yield),
                isLast = state.ingredients.isEmpty() && state.instructions.isEmpty(),
                onValueChange = { onEvent(AddRecipeScreenEvent.RecipeYieldInput(it)) },
            )
        }

        itemsIndexed(state.ingredients) { index, ingredient ->
            AddRecipeInputField(
                input = ingredient,
                label = stringResource(id = R.string.fragment_add_recipe_ingredient_hint),
                isLast = state.ingredients.lastIndex == index && state.instructions.isEmpty(),
                onValueChange = {
                    onEvent(AddRecipeScreenEvent.IngredientInput(index, it))
                },
                onRemoveClick = {
                    onEvent(AddRecipeScreenEvent.RemoveIngredientClick(index))
                },
            )
        }

        item {
            Button(
                onClick = {
                    onEvent(AddRecipeScreenEvent.AddIngredientClick)
                },
            ) {
                Text(
                    text = stringResource(id = R.string.fragment_add_recipe_new_ingredient),
                )
            }
        }

        itemsIndexed(state.instructions) { index, instruction ->
            AddRecipeInputField(
                input = instruction,
                label = stringResource(id = R.string.fragment_add_recipe_instruction_hint),
                isLast = state.instructions.lastIndex == index,
                onValueChange = {
                    onEvent(
                        AddRecipeScreenEvent.InstructionInput(index, it)
                    )
                },
                onRemoveClick = {
                    onEvent(AddRecipeScreenEvent.RemoveInstructionClick(index))
                },
            )
        }

        item {
            Button(
                onClick = {
                    onEvent(AddRecipeScreenEvent.AddInstructionClick)
                },
            ) {
                Text(
                    text = stringResource(id = R.string.fragment_add_recipe_new_instruction),
                )
            }
        }

        item {
            AddRecipeSwitch(
                name = R.string.fragment_add_recipe_public_recipe,
                checked = state.isPublicRecipe,
                onCheckedChange = { onEvent(AddRecipeScreenEvent.PublicRecipeToggle) },
            )
        }

        item {
            AddRecipeSwitch(
                name = R.string.fragment_add_recipe_disable_comments,
                checked = state.disableComments,
                onCheckedChange = { onEvent(AddRecipeScreenEvent.DisableCommentsToggle) },
            )
        }

        item {
            AddRecipeActions(
                state = state,
                onEvent = onEvent,
            )
        }
    }
}

@Composable
private fun AddRecipeActions(
    state: AddRecipeScreenState,
    onEvent: (AddRecipeScreenEvent) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Dimens.Large, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(
            enabled = state.clearButtonEnabled,
            onClick = {
                onEvent(AddRecipeScreenEvent.ClearInputClick)
            },
        ) {
            Text(
                text = stringResource(id = R.string.fragment_add_recipe_clear_button),
            )
        }

        Button(
            enabled = state.saveButtonEnabled,
            onClick = {
                onEvent(AddRecipeScreenEvent.SaveRecipeClick)
            },
        ) {
            Text(
                text = stringResource(id = R.string.fragment_add_recipe_save_button),
            )
        }
    }
}

@Composable
private fun AddRecipeSwitch(
    @StringRes name: Int,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Dimens.Medium),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(id = name),
        )

        Spacer(modifier = Modifier.weight(1f))

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
    }
}

@Composable
private fun AddRecipeInputField(
    input: String,
    label: String,
    isLast: Boolean,
    onValueChange: (String) -> Unit,
    onRemoveClick: (() -> Unit)? = null,
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),
        value = input,
        onValueChange = onValueChange,
        label = {
            Text(text = label)
        },
        trailingIcon = {
            if (onRemoveClick != null) {
                IconButton(onClick = onRemoveClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = if (isLast) ImeAction.Done else ImeAction.Next,
        )
    )
}

@ColorSchemePreview
@Composable
private fun AddRecipeScreenPreview() {
    AppTheme {
        val snackbarHostState = remember { SnackbarHostState() }
        AddRecipeScreen(
            snackbarHostState = snackbarHostState,
            state = AddRecipeScreenState(),
            onEvent = {},
        )
    }
}