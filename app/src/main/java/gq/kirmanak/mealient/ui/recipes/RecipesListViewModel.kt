package gq.kirmanak.mealient.ui.recipes

import androidx.lifecycle.*
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.data.recipes.RecipeRepo
import gq.kirmanak.mealient.extensions.valueUpdatesOnly
import gq.kirmanak.mealient.logging.Logger
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class RecipesListViewModel @Inject constructor(
    private val recipeRepo: RecipeRepo,
    authRepo: AuthRepo,
    private val logger: Logger,
) : ViewModel() {

    val pagingData = recipeRepo.createPager().flow.cachedIn(viewModelScope)

    private val _isAuthorized = MutableLiveData<Boolean?>(null)
    val isAuthorized: LiveData<Boolean?> = _isAuthorized

    init {
        authRepo.isAuthorizedFlow.valueUpdatesOnly().onEach {
            logger.v { "Authorization state changed to $it" }
            _isAuthorized.postValue(it)
        }.launchIn(viewModelScope)
    }

    fun onAuthorizationChangeHandled() {
        logger.v { "onAuthorizationSuccessHandled() called" }
        _isAuthorized.postValue(null)
    }

    fun refreshRecipeInfo(recipeSlug: String): LiveData<Result<Unit>> {
        logger.v { "refreshRecipeInfo called with: recipeSlug = $recipeSlug" }
        return liveData {
            val result = recipeRepo.refreshRecipeInfo(recipeSlug)
            logger.v { "refreshRecipeInfo: emitting $result" }
            emit(result)
        }
    }
}