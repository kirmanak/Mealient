package gq.kirmanak.mealient.ui.share

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.data.share.ShareRecipeRepo
import gq.kirmanak.mealient.datasource.runCatchingExceptCancel
import gq.kirmanak.mealient.logging.Logger
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShareRecipeViewModel @Inject constructor(
    private val shareRecipeRepo: ShareRecipeRepo,
    private val logger: Logger,
) : ViewModel() {

    private val _saveOperationResult = MutableLiveData<Result<String>>()
    val saveOperationResult: LiveData<Result<String>> get() = _saveOperationResult

    fun saveRecipeByURL(url: CharSequence) {
        logger.v { "saveRecipeByURL() called with: url = $url" }
        viewModelScope.launch {
            runCatchingExceptCancel {
                shareRecipeRepo.saveRecipeByURL(url)
            }.onSuccess {
                logger.d { "Successfully saved recipe by URL" }
                _saveOperationResult.postValue(Result.success(it))
            }.onFailure {
                logger.e(it) { "Can't save recipe by URL" }
                _saveOperationResult.postValue(Result.failure(it))
            }
        }
    }
}