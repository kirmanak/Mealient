package gq.kirmanak.mealient.ui.share

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.data.share.ShareRecipeRepo
import gq.kirmanak.mealient.datasource.runCatchingExceptCancel
import gq.kirmanak.mealient.logging.Logger
import gq.kirmanak.mealient.ui.OperationUiState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShareRecipeViewModel @Inject constructor(
    private val shareRecipeRepo: ShareRecipeRepo,
    private val logger: Logger,
) : ViewModel() {

    private val _saveResult = MutableLiveData<OperationUiState<String>>(OperationUiState.Initial())
    val saveResult: LiveData<OperationUiState<String>> get() = _saveResult

    fun saveRecipeByURL(url: CharSequence) {
        logger.v { "saveRecipeByURL() called with: url = $url" }
        _saveResult.postValue(OperationUiState.Progress())
        viewModelScope.launch {
            val result = runCatchingExceptCancel { shareRecipeRepo.saveRecipeByURL(url) }
                .onSuccess { logger.d { "Successfully saved recipe by URL" } }
                .onFailure { logger.e(it) { "Can't save recipe by URL" } }
            _saveResult.postValue(OperationUiState.fromResult(result))
        }
    }
}