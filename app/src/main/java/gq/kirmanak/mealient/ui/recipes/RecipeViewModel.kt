package gq.kirmanak.mealient.ui.recipes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.data.recipes.RecipeRepo
import gq.kirmanak.mealient.logging.Logger
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.runningReduce
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    recipeRepo: RecipeRepo,
    authRepo: AuthRepo,
    private val logger: Logger,
) : ViewModel() {

    val pagingData = recipeRepo.createPager().flow.cachedIn(viewModelScope)

    private val _isAuthorized = MutableLiveData<Boolean?>(null)
    val isAuthorized: LiveData<Boolean?> = _isAuthorized

    init {
        authRepo.isAuthorizedFlow.runningReduce { wasAuthorized, isAuthorized ->
            logger.v { "Authorization state changed from $wasAuthorized to $isAuthorized" }
            if (wasAuthorized != isAuthorized) {
                _isAuthorized.postValue(isAuthorized)
            }
            isAuthorized
        }.launchIn(viewModelScope)
    }

    fun onAuthorizationChangeHandled() {
        logger.v { "onAuthorizationSuccessHandled() called" }
        _isAuthorized.postValue(null)
    }
}