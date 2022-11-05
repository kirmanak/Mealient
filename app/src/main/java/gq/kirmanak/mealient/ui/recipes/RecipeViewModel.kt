package gq.kirmanak.mealient.ui.recipes

import androidx.lifecycle.*
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.data.recipes.RecipeRepo
import gq.kirmanak.mealient.database.recipe.entity.FullRecipeEntity
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity
import gq.kirmanak.mealient.datasource.runCatchingExceptCancel
import gq.kirmanak.mealient.extensions.valueUpdatesOnly
import gq.kirmanak.mealient.logging.Logger
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
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

    fun loadRecipeInfo(
        summaryEntity: RecipeSummaryEntity
    ): LiveData<Result<FullRecipeEntity>> = liveData {
        val result = runCatchingExceptCancel {
            recipeRepo.loadRecipeInfo(summaryEntity.remoteId, summaryEntity.slug)
        }
        emit(result)
    }
}