package gq.kirmanak.mealient.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.data.add.AddRecipeRepo
import gq.kirmanak.mealient.datasource.models.AddRecipeRequest
import gq.kirmanak.mealient.extensions.runCatchingExceptCancel
import gq.kirmanak.mealient.logging.Logger
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddRecipeViewModel @Inject constructor(
    private val addRecipeRepo: AddRecipeRepo,
    private val logger: Logger,
) : ViewModel() {

    private val _addRecipeResultChannel = Channel<Boolean>(Channel.UNLIMITED)
    val addRecipeResult: Flow<Boolean> get() = _addRecipeResultChannel.receiveAsFlow()

    private val _preservedAddRecipeRequestChannel = Channel<AddRecipeRequest>(Channel.UNLIMITED)
    val preservedAddRecipeRequest: Flow<AddRecipeRequest>
        get() = _preservedAddRecipeRequestChannel.receiveAsFlow()

    fun loadPreservedRequest() {
        logger.v { "loadPreservedRequest() called" }
        viewModelScope.launch { doLoadPreservedRequest() }
    }

    private suspend fun doLoadPreservedRequest() {
        logger.v { "doLoadPreservedRequest() called" }
        val request = addRecipeRepo.addRecipeRequestFlow.first()
        logger.d { "doLoadPreservedRequest: request = $request" }
        _preservedAddRecipeRequestChannel.send(request)
    }

    fun clear() {
        logger.v { "clear() called" }
        viewModelScope.launch {
            addRecipeRepo.clear()
            doLoadPreservedRequest()
        }
    }

    fun preserve(request: AddRecipeRequest) {
        logger.v { "preserve() called with: request = $request" }
        viewModelScope.launch { addRecipeRepo.preserve(request) }
    }

    fun saveRecipe() {
        logger.v { "saveRecipe() called" }
        viewModelScope.launch {
            val isSuccessful = runCatchingExceptCancel { addRecipeRepo.saveRecipe() }
                .fold(onSuccess = { true }, onFailure = { false })
            logger.d { "saveRecipe: isSuccessful = $isSuccessful" }
            _addRecipeResultChannel.send(isSuccessful)
        }
    }
}
