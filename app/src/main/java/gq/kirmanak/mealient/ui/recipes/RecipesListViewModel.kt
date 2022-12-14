package gq.kirmanak.mealient.ui.recipes

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import gq.kirmanak.mealient.data.auth.AuthRepo
import gq.kirmanak.mealient.data.recipes.RecipeRepo
import gq.kirmanak.mealient.database.recipe.entity.RecipeSummaryEntity
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
    val showFavoriteIcon = authRepo.isAuthorizedFlow.asLiveData()

    init {
        authRepo.isAuthorizedFlow.valueUpdatesOnly().onEach { hasAuthorized ->
            logger.v { "Authorization state changed to $hasAuthorized" }
            if (hasAuthorized) recipeRepo.refreshRecipes()
        }.launchIn(viewModelScope)
    }

    fun refreshRecipeInfo(recipeSlug: String): LiveData<Result<Unit>> {
        logger.v { "refreshRecipeInfo called with: recipeSlug = $recipeSlug" }
        return liveData {
            val result = recipeRepo.refreshRecipeInfo(recipeSlug)
            logger.v { "refreshRecipeInfo: emitting $result" }
            emit(result)
        }
    }

    fun onFavoriteIconClick(recipeSummaryEntity: RecipeSummaryEntity) = liveData {
        logger.v { "onFavoriteIconClick() called with: recipeSummaryEntity = $recipeSummaryEntity" }
        recipeRepo.updateIsRecipeFavorite(
            recipeSlug = recipeSummaryEntity.slug,
            isFavorite = recipeSummaryEntity.isFavorite.not(),
        ).also { emit(it) }
    }

    fun onDeleteConfirm(recipeSummaryEntity: RecipeSummaryEntity) = liveData {
        logger.v { "onDeleteConfirm() called with: recipeSummaryEntity = $recipeSummaryEntity" }
        recipeRepo.deleteRecipe(recipeSummaryEntity.slug).also { emit(it) }
    }
}