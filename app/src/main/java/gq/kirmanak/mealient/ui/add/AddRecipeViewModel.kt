package gq.kirmanak.mealient.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.data.add.AddRecipeRepo
import gq.kirmanak.mealient.data.add.models.AddRecipeRequest
import gq.kirmanak.mealient.extensions.runCatchingExceptCancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AddRecipeViewModel @Inject constructor(
    private val addRecipeRepo: AddRecipeRepo,
) : ViewModel() {

    private val _addRecipeResultChannel = Channel<Boolean>(Channel.UNLIMITED)
    val addRecipeResult: Flow<Boolean> get() = _addRecipeResultChannel.receiveAsFlow()

    private val _preservedAddRecipeRequestChannel = Channel<AddRecipeRequest>(Channel.UNLIMITED)
    val preservedAddRecipeRequest: Flow<AddRecipeRequest>
        get() = _preservedAddRecipeRequestChannel.receiveAsFlow()

    fun loadPreservedRequest() {
        Timber.v("loadPreservedRequest() called")
        viewModelScope.launch { doLoadPreservedRequest() }
    }

    private suspend fun doLoadPreservedRequest() {
        Timber.v("doLoadPreservedRequest() called")
        val request = addRecipeRepo.addRecipeRequestFlow.first()
        Timber.d("doLoadPreservedRequest: request = $request")
        _preservedAddRecipeRequestChannel.send(request)
    }

    fun clear() {
        Timber.v("clear() called")
        viewModelScope.launch {
            addRecipeRepo.clear()
            doLoadPreservedRequest()
        }
    }

    fun preserve(request: AddRecipeRequest) {
        Timber.v("preserve() called with: request = $request")
        viewModelScope.launch { addRecipeRepo.preserve(request) }
    }

    fun saveRecipe() {
        Timber.v("saveRecipe() called")
        viewModelScope.launch {
            val isSuccessful = runCatchingExceptCancel { addRecipeRepo.saveRecipe() }
                .fold(onSuccess = { true }, onFailure = { false })
            Timber.d("saveRecipe: isSuccessful = $isSuccessful")
            _addRecipeResultChannel.send(isSuccessful)
        }
    }
}
