package gq.kirmanak.mealient.ui.add

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import gq.kirmanak.mealient.R
import gq.kirmanak.mealient.ui.AppTheme
import gq.kirmanak.mealient.ui.Dimens
import gq.kirmanak.mealient.ui.components.TopProgressIndicator
import gq.kirmanak.mealient.ui.preview.ColorSchemePreview

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun AddRecipeScreen(
    state: AddRecipeScreenState,
    onEvent: (AddRecipeScreenEvent) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { containerPadding ->
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
            modifier = Modifier
                .padding(containerPadding)
                .consumeWindowInsets(containerPadding),
            isLoading = state.isLoading,
        ) {
            AddRecipeScreenContent(
                state = state,
                onEvent = onEvent,
            )
        }
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
                onValueChange = { onEvent(AddRecipeScreenEvent.RecipeNameInput(it)) },
            )
        }

        item {
            AddRecipeInputField(
                input = state.recipeDescriptionInput,
                label = stringResource(id = R.string.fragment_add_recipe_recipe_description),
                onValueChange = { onEvent(AddRecipeScreenEvent.RecipeDescriptionInput(it)) },
            )
        }

        item {
            AddRecipeInputField(
                input = state.recipeYieldInput,
                label = stringResource(id = R.string.fragment_add_recipe_recipe_yield),
                onValueChange = { onEvent(AddRecipeScreenEvent.RecipeYieldInput(it)) },
            )
        }

        itemsIndexed(state.ingredients) { index, ingredient ->
            AddRecipeInputField(
                input = ingredient,
                label = stringResource(id = R.string.fragment_add_recipe_ingredient_hint),
                onValueChange = {
                    onEvent(
                        AddRecipeScreenEvent.IngredientInput(
                            index,
                            it
                        )
                    )
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
                onValueChange = {
                    onEvent(
                        AddRecipeScreenEvent.InstructionInput(index, it)
                    )
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
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),
        value = input,
        onValueChange = onValueChange,
        label = {
            Text(text = label)
        },
    )
}

@ColorSchemePreview
@Composable
private fun AddRecipeScreenPreview() {
    AppTheme {
        var state by remember { mutableStateOf(AddRecipeScreenState()) }
        AddRecipeScreen(
            state = AddRecipeScreenState(),
            onEvent = {
                when (it) {
                    is AddRecipeScreenEvent.RecipeNameInput -> {
                        state = state.copy(recipeNameInput = it.input)
                    }

                    is AddRecipeScreenEvent.RecipeDescriptionInput -> {
                        state = state.copy(recipeDescriptionInput = it.input)
                    }

                    is AddRecipeScreenEvent.RecipeYieldInput -> {
                        state = state.copy(recipeYieldInput = it.input)
                    }

                    is AddRecipeScreenEvent.PublicRecipeToggle -> {
                        state = state.copy(isPublicRecipe = !state.isPublicRecipe)
                    }

                    is AddRecipeScreenEvent.DisableCommentsToggle -> {
                        state = state.copy(disableComments = !state.disableComments)
                    }

                    is AddRecipeScreenEvent.AddIngredientClick -> {
                        state = state.copy(ingredients = state.ingredients + "")
                    }

                    is AddRecipeScreenEvent.AddInstructionClick -> {
                        state = state.copy(instructions = state.instructions + "")
                    }

                    is AddRecipeScreenEvent.SaveRecipeClick -> {
                        state = state.copy(isLoading = true)
                    }

                    is AddRecipeScreenEvent.IngredientInput -> {
                        state = state.copy(
                            ingredients = state.ingredients.toMutableList().apply {
                                set(it.ingredientIndex, it.input)
                            }
                        )
                    }

                    is AddRecipeScreenEvent.InstructionInput -> {
                        state = state.copy(
                            instructions = state.instructions.toMutableList().apply {
                                set(it.instructionIndex, it.input)
                            }
                        )
                    }

                    is AddRecipeScreenEvent.ClearInputClick -> {
                        state = state.copy(
                            recipeNameInput = "",
                            recipeDescriptionInput = "",
                            recipeYieldInput = "",
                            ingredients = emptyList(),
                            instructions = emptyList(),
                            isPublicRecipe = false,
                            disableComments = false,
                            saveButtonEnabled = false,
                        )
                    }

                    is AddRecipeScreenEvent.SnackbarShown -> Unit
                }
            },
        )
    }
}