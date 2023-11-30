package gq.kirmanak.mealient.ui.add

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.data.add.AddRecipeRepo
import gq.kirmanak.mealient.datasource.models.AddRecipeInfo
import gq.kirmanak.mealient.datasource.models.AddRecipeIngredientInfo
import gq.kirmanak.mealient.datasource.models.AddRecipeInstructionInfo
import gq.kirmanak.mealient.datasource.models.AddRecipeSettingsInfo
import gq.kirmanak.mealient.datasource.runCatchingExceptCancel
import gq.kirmanak.mealient.logging.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class AddRecipeViewModel @Inject constructor(
    private val addRecipeRepo: AddRecipeRepo,
    private val logger: Logger,
) : ViewModel() {

    private val _screenState = MutableStateFlow(AddRecipeScreenState())
    val screenState: StateFlow<AddRecipeScreenState> get() = _screenState.asStateFlow()

    init {
        logger.v { "init() called" }
        viewModelScope.launch { doLoadPreservedRequest() }
    }

    @VisibleForTesting
    suspend fun doLoadPreservedRequest() {
        logger.v { "doLoadPreservedRequest() called" }
        val request = addRecipeRepo.addRecipeRequestFlow.first()
        logger.d { "doLoadPreservedRequest: request = $request" }
        _screenState.update { state ->
            state.copy(
                recipeNameInput = request.name,
                recipeDescriptionInput = request.description,
                recipeYieldInput = request.recipeYield,
                isPublicRecipe = request.settings.public,
                disableComments = request.settings.disableComments,
                ingredients = request.recipeIngredient.map { it.note },
                instructions = request.recipeInstructions.map { it.text },
                saveButtonEnabled = request.name.isNotBlank(),
            )
        }
    }

    fun onEvent(event: AddRecipeScreenEvent) {
        logger.v { "onEvent() called with: event = $event" }
        when (event) {
            is AddRecipeScreenEvent.AddIngredientClick -> {
                _screenState.update {
                    it.copy(ingredients = it.ingredients + "")
                }
            }

            is AddRecipeScreenEvent.AddInstructionClick -> {
                _screenState.update {
                    it.copy(instructions = it.instructions + "")
                }
            }

            is AddRecipeScreenEvent.DisableCommentsToggle -> {
                _screenState.update {
                    it.copy(disableComments = !it.disableComments)
                }
                preserve()
            }

            is AddRecipeScreenEvent.PublicRecipeToggle -> {
                _screenState.update {
                    it.copy(isPublicRecipe = !it.isPublicRecipe)
                }
                preserve()
            }

            is AddRecipeScreenEvent.ClearInputClick -> {
                logger.v { "clear() called" }
                viewModelScope.launch {
                    addRecipeRepo.clear()
                    doLoadPreservedRequest()
                }
            }

            is AddRecipeScreenEvent.IngredientInput -> {
                _screenState.update {
                    val mutableIngredientsList = it.ingredients.toMutableList()
                    mutableIngredientsList[event.ingredientIndex] = event.input
                    it.copy(ingredients = mutableIngredientsList)
                }
                preserve()
            }

            is AddRecipeScreenEvent.InstructionInput -> {
                _screenState.update {
                    val mutableInstructionsList = it.instructions.toMutableList()
                    mutableInstructionsList[event.instructionIndex] = event.input
                    it.copy(instructions = mutableInstructionsList)
                }
                preserve()
            }

            is AddRecipeScreenEvent.RecipeDescriptionInput -> {
                _screenState.update {
                    it.copy(recipeDescriptionInput = event.input)
                }
                preserve()
            }

            is AddRecipeScreenEvent.RecipeNameInput -> {
                _screenState.update {
                    it.copy(
                        recipeNameInput = event.input,
                        saveButtonEnabled = event.input.isNotBlank(),
                    )
                }
                preserve()
            }

            is AddRecipeScreenEvent.RecipeYieldInput -> {
                _screenState.update {
                    it.copy(recipeYieldInput = event.input)
                }
                preserve()
            }

            is AddRecipeScreenEvent.SaveRecipeClick -> {
                saveRecipe()
            }

            is AddRecipeScreenEvent.SnackbarShown -> {
                _screenState.update {
                    it.copy(snackbarMessage = null)
                }
            }

            is AddRecipeScreenEvent.RemoveIngredientClick -> {
                _screenState.update {
                    val mutableIngredientsList = it.ingredients.toMutableList()
                    mutableIngredientsList.removeAt(event.ingredientIndex)
                    it.copy(ingredients = mutableIngredientsList)
                }
                preserve()
            }

            is AddRecipeScreenEvent.RemoveInstructionClick -> {
                _screenState.update {
                    val mutableInstructionsList = it.instructions.toMutableList()
                    mutableInstructionsList.removeAt(event.instructionIndex)
                    it.copy(instructions = mutableInstructionsList)
                }
                preserve()
            }
        }
    }

    private fun saveRecipe() {
        logger.v { "saveRecipe() called" }
        _screenState.update {
            it.copy(
                isLoading = true,
                saveButtonEnabled = false,
                clearButtonEnabled = false,
            )
        }
        viewModelScope.launch {
            val isSuccessful = runCatchingExceptCancel { addRecipeRepo.saveRecipe() }.isSuccess
            _screenState.update {
                it.copy(
                    isLoading = false,
                    saveButtonEnabled = true,
                    clearButtonEnabled = true,
                    snackbarMessage = if (isSuccessful) {
                        AddRecipeSnackbarMessage.Success
                    } else {
                        AddRecipeSnackbarMessage.Error
                    }
                )
            }
        }
    }

    private fun preserve() {
        logger.v { "preserve() called" }
        viewModelScope.launch {
            val request = AddRecipeInfo(
                name = screenState.value.recipeNameInput,
                description = screenState.value.recipeDescriptionInput,
                recipeYield = screenState.value.recipeYieldInput,
                recipeIngredient = screenState.value.ingredients.map {
                    AddRecipeIngredientInfo(it)
                },
                recipeInstructions = screenState.value.instructions.map {
                    AddRecipeInstructionInfo(it)
                },
                settings = AddRecipeSettingsInfo(
                    public = screenState.value.isPublicRecipe,
                    disableComments = screenState.value.disableComments,
                )
            )
            addRecipeRepo.preserve(request)
        }
    }
}
